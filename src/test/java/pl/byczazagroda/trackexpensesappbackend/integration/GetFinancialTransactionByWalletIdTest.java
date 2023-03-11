package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class GetFinancialTransactionByWalletIdTest extends BaseIntegrationTestIT {

    @Autowired
    FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    WalletRepository walletRepository;

    @BeforeEach
    public void clearDb(){
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    public void testGetFinancialTransactionsByWalletID_whenWalletIdIsCorrect_thenReturnListOfFinancialTransactionDTO() throws Exception {
        Wallet wallet = createTestWallet();
        FinancialTransaction financialTransaction = createTestFinancialTransaction(wallet);
        mockMvc.perform(get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("walletId", String.valueOf(wallet.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[0].id").value(financialTransaction.getId()))
                .andExpect(jsonPath("$.[0].amount").value(financialTransaction.getAmount()))
                .andExpect(jsonPath("$.[0].description").value(financialTransaction.getDescription()))
                .andExpect(jsonPath("$.[0].type").value(financialTransaction.getType().name()))
                .andExpect(jsonPath("$.[0].date").value(financialTransaction.getDate().toString()));
        Assertions.assertEquals(1, financialTransactionRepository.count());
        Assertions.assertEquals(1, walletRepository.count());
    }

    @Test
    public void testGetFinancialTransactionsByWalletID_whenWalletIdIsIncorrect_thenReturnErrorResponseDTO() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("walletId", "1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        Assertions.assertEquals(0, financialTransactionRepository.count());
        Assertions.assertEquals(0, walletRepository.count());
    }

    private Wallet createTestWallet() {
        return walletRepository.save(new Wallet("test_wallet"));
    }

    private FinancialTransaction createTestFinancialTransaction(Wallet wallet) {
        return financialTransactionRepository.save(FinancialTransaction.builder()
                .wallet(wallet)
                .amount(BigDecimal.valueOf(2.0))
                .date(Instant.ofEpochMilli(1641828224000L))
                .type(FinancialTransactionType.INCOME)
                .description("test description")
                .build());
    }

}
