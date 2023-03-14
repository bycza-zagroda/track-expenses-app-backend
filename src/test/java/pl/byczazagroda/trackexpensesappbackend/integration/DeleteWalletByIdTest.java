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
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class DeleteWalletByIdTest extends BaseIntegrationTestIT {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    FinancialTransactionRepository financialTransactionRepository;

    @BeforeEach
    void clearDatabase() {
        walletRepository.deleteAll();
    }

    @DisplayName("Should delete wallet from database and return status isOk")
    @Test
    void testDeleteWalletByIdAPI_whenWalletIdIsCorrect_thenShouldReturnAcceptAndDeleteRecord () throws Exception {
        Wallet wallet = walletRepository.save(new Wallet("Test Wallet"));
        createTestFinancialTransaction(wallet);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/wallets/{id}", wallet.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertEquals(0, walletRepository.count());
    }

    @DisplayName("Should return is Not Found error when Id does not exist")
    @Test
    void testDeleteWalletById_whenWalletIdIsIncorrect_thenShouldReturnNotFoundError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/wallets/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.W003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.W003.getBusinessMessage()));
        Assertions.assertEquals(0, walletRepository.count());
    }

    private void createTestFinancialTransaction(Wallet wallet){
        financialTransactionRepository.save(FinancialTransaction.builder()
                .wallet(wallet)
                .amount(new BigDecimal("2.0"))
                .date(Instant.ofEpochMilli(0L))
                .type(FinancialTransactionType.INCOME)
                .description("Test transaction")
                .build());
    }

}