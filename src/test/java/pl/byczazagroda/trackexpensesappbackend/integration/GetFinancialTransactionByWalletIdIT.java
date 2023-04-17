package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class GetFinancialTransactionByWalletIdIT extends BaseIntegrationTestIT {

    @Autowired
    FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    WalletRepository walletRepository;

    @BeforeEach
    public void clearTestDB() {
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    @DisplayName("when wallet id is correct returns List of financial transactions DTO related to wallet")
    public void givenValidWalletId_whenGetFinancialTransactionsByWalletId_thenCorrectResponse() throws Exception {
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
    @DisplayName("when wallet id is incorrect returns error response dto and has 404 status code")
    public void givenInvalidWalletId_whenGetFinancialTransactionsByWalletId_thenNotFoundStatusCode() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("walletId", "1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(ErrorCode.W003.getBusinessStatus()))
                .andExpect(jsonPath("$.message").value(ErrorCode.W003.getBusinessMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorCode.W003.getBusinessStatusCode()));

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
                .date(Instant.ofEpochSecond(1L))
                .type(FinancialTransactionType.INCOME)
                .description("test description")
                .build());
    }

}
