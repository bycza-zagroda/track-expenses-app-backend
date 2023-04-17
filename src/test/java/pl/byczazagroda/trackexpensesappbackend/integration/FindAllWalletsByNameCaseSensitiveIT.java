package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import static org.hamcrest.Matchers.hasSize;

public class FindAllWalletsByNameCaseSensitiveIT extends BaseIntegrationTestIT {

    static private final String WALLET_NAME = "wallet";
    static private final String WALLET_NAME_TOO_LONG = "The quick, brown fox jumps over";

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    FinancialTransactionRepository financialTransactionRepository;

    @BeforeEach
    void clearDatabase() {
        walletRepository.deleteAll();
        financialTransactionRepository.deleteAll();
    }

    @DisplayName("Find all Wallets with corresponding search name, ignoring case")
    @Test
    void testFindAllWalletsByNameIgnoringCaseAPI_whenSearchNameIsProvided_thenShouldReturnAllWalletsWithSearchNameIgnoringCase()
            throws Exception {
        Wallet wallet1 = walletRepository.save(new Wallet("Wallet"));
        Wallet wallet2 = walletRepository.save(new Wallet("wallet"));
        Wallet wallet3 = walletRepository.save(new Wallet("WALLET"));
        Wallet wallet4 = walletRepository.save(new Wallet("Bag"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/wallets/wallets/{name}", WALLET_NAME).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(wallet1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(wallet2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].id").value(wallet3.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(wallet1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value(wallet2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].name").value(wallet3.getName()));

        Assertions.assertEquals(4, walletRepository.count());
    }

    @DisplayName("When search name is too long then empty array and error - bad request should be returned")
    @Test
    void testFindAllWalletsByNameIgnoringCaseAPI_whenSearchNameTooLong_thenShouldReturnTEA003Error() throws Exception {
        Wallet wallet1 = walletRepository.save(new Wallet("Wallet"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/wallets/wallets/{name}", WALLET_NAME_TOO_LONG).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()));

        Assertions.assertEquals(1, walletRepository.count());
    }

    @DisplayName("When search name does not exist should return null array")
    @Test
    void testFindAllWalletsByNameIgnoringCaseAPI_whenSearchNameDoesNotExistInDB_thenShouldReturnNullArray() throws Exception {
        Wallet wallet4 = walletRepository.save(new Wallet("Bag"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/wallets/wallets/{name}", WALLET_NAME).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(0)));
    }

}