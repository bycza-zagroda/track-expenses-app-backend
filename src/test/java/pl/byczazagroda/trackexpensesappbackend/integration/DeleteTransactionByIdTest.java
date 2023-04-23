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
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.Instant;

public class DeleteTransactionByIdTest extends BaseIntegrationTestIT {


    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void clearDatabase() {
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @DisplayName("Should return deleted transaction successfully")
    @Test
    public void testDeleteFinancialTransactionById_whenDeletedFinancialTransactionWithExistingId_thenReturnDeletedSuccesfully()
            throws Exception {
        Wallet wallet = walletRepository.save(new Wallet("Test Wallet"));
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet, "Test Transaction");
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/transactions/{id}", testFinancialTransaction.getId())
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertEquals(0, financialTransactionRepository.count());

    }

    @DisplayName("Should return is Not Found error when Id does not exist in a database")
    @Test
    void testDeleteFinancialTransactionById_whenFinancialTransactionIdIsIncorrect_thenShouldReturnNotFoundError() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet("Test Wallet"));
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet, "Test Transaction");
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/transactions/{id}", 100L)
                .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
        Assertions.assertEquals(1, financialTransactionRepository.count());
    }

    private FinancialTransaction createTestFinancialTransaction(Wallet wallet, String description) {
        return financialTransactionRepository.save(FinancialTransaction.builder()
                .wallet(wallet)
                .amount(new BigDecimal("5.0"))
                .date(Instant.ofEpochSecond(1L))
                .type(FinancialTransactionType.INCOME)
                .description(description)
                .build());
    }


}
