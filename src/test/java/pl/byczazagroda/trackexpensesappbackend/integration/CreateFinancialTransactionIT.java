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
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class CreateFinancialTransactionIT extends BaseIntegrationTestIT {

    @Autowired
    FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    WalletRepository walletRepository;

    @BeforeEach
    void clearDatabase() {
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @DisplayName("Should successfully create financial transaction")
    @Test
    void testCreateFinancialTransaction_whenProvidedCorrectData_thenShouldSaveFinancialTransactionInDatabase() throws Exception {
        Wallet savedWallet = walletRepository.save(new Wallet("Test wallet"));
        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                savedWallet.getId(),
                new BigDecimal("5.0"),
                "Test Description",
                Instant.ofEpochSecond(1L),
                FinancialTransactionType.EXPENSE);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCreateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.amount").value(financialTransactionCreateDTO.amount()))
                .andExpect(jsonPath("$.type").value(financialTransactionCreateDTO.type().toString()))
                .andExpect(jsonPath("$.description").value(financialTransactionCreateDTO.description().toString()));

        Assertions.assertEquals(1, walletRepository.count());
        Assertions.assertEquals(1, financialTransactionRepository.count());
    }

    @DisplayName("Should return Wallet Not Found message when creating financial transaction wallet Id that doesnt exist in database")
    @Test
    void testCreateFinancialTransaction_whenCreatingFinancialTransactionIdWalletNotFound_thenReturnIsNotFoundAndErrorMessage() throws Exception {
        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                1L,
                new BigDecimal("5.0"),
                "Test Description",
                Instant.ofEpochSecond(1L),
                FinancialTransactionType.EXPENSE);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCreateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.W003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.W003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.W003.getBusinessStatusCode()));

        Assertions.assertEquals(0, walletRepository.count());
        Assertions.assertEquals(0, financialTransactionRepository.count());
    }

    @DisplayName("Should return bad request and validation failed error when creating financial transaction with amount exceeding limit")
    @Test
    void testCreateFinancialTransaction_whenAmountExceedsLimit_thenReturnBadRequestAndErrorValidationFailed() throws Exception {
        Wallet savedWallet = walletRepository.save(new Wallet("Test wallet"));
        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                savedWallet.getId(),
                new BigDecimal("55555555555555555555555.0"),
                "Test Description",
                Instant.ofEpochSecond(1L),
                FinancialTransactionType.EXPENSE);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCreateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));

        Assertions.assertEquals(1, walletRepository.count());
        Assertions.assertEquals(0, financialTransactionRepository.count());
    }

}