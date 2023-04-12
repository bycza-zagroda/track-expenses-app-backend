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

public class FindTransactionByIDTest extends BaseIntegrationTestIT {

    @Autowired
    FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    WalletRepository walletRepository;

    @BeforeEach
    void clearDatabase() {
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @DisplayName("Should return proper financial transaction when search Id exist in database")
    @Test
    public void testGetFinancialTransactionById_whenFindingTransactionWithExistingId_thenReturnFinancialTransactionWithCorrespondingId() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet("TestWallet"));
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet, "Test1");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/{id}", testFinancialTransaction.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testFinancialTransaction.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(testFinancialTransaction.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(testFinancialTransaction.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(testFinancialTransaction.getType().toString()));

        Assertions.assertEquals(1, financialTransactionRepository.count());
    }

    @DisplayName("Should return status NOT_FOUND when search Id does not exist in database")
    @Test
    public void testGetFinancialTransactionById_whenSearchIdDoesNotExistInDatabase_thenReturnErrorNotFound() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet("TestWallet"));
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet, "Test1");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/{id}", 999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.FT001.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.FT001.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.FT001.getBusinessStatusCode()));

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
