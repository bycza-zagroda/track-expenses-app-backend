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
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class UpdateTransactionByIdIT extends BaseIntegrationTestIT {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void clearDatabase() {
        walletRepository.deleteAll();
        financialTransactionRepository.deleteAll();
    }

    @DisplayName("Update financial transaction with new data provided in DTO when Id found in database")
    @Test
    void updateExistingFinancialTransaction_whenDataProvidedInDTOAndIdIsFoundInDB_thenUpdateExistingFinancialTransactionWithRespectiveId() throws Exception {
        Wallet wallet = walletRepository.save(new Wallet("Test Wallet"));
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet);
        FinancialTransactionUpdateDTO updateDTO = new FinancialTransactionUpdateDTO(
                new BigDecimal("5.0"),
                Instant.ofEpochSecond(2L),
                "Updated DTO Description",
                FinancialTransactionType.EXPENSE,
                null);

        mockMvc.perform(MockMvcRequestBuilders.
                        patch("/api/transactions/{id}", testFinancialTransaction.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status().isOk())
                .andExpect(jsonPath("$.id").value(testFinancialTransaction.getId()))
                .andExpect(jsonPath("$.amount").value(updateDTO.amount()))
                .andExpect(jsonPath("$.date").value(updateDTO.date().toString()))
                .andExpect(jsonPath("$.type").value(updateDTO.type().name()))
                .andExpect(jsonPath("$.description").value(updateDTO.description()));

        Assertions.assertEquals(1, financialTransactionRepository.count());
        Assertions.assertEquals(1, walletRepository.count());
    }

    @DisplayName("Return isNotFound status when ID not found in database")
    @Test
    void testUpdateTransactionById_whenIdIsNotFoundInDB_thenReturnIsNotFound() throws Exception {
        FinancialTransactionUpdateDTO updateDTO = new FinancialTransactionUpdateDTO(
                new BigDecimal("5.0"),
                Instant.ofEpochSecond(2L),
                "Updated DTO Description",
                FinancialTransactionType.EXPENSE,
                null);

        mockMvc.perform(MockMvcRequestBuilders.
                        patch("/api/transactions/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.FT001.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.FT001.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.FT001.getBusinessStatusCode()));

        Assertions.assertEquals(0, financialTransactionRepository.count());
        Assertions.assertEquals(0, walletRepository.count());
    }

    private FinancialTransaction createTestFinancialTransaction(Wallet wallet) {
        return financialTransactionRepository.save(FinancialTransaction.builder()
                .wallet(wallet)
                .amount(new BigDecimal("10.0"))
                .date(Instant.ofEpochSecond(1L))
                .type(FinancialTransactionType.INCOME)
                .description("Test description")
                .build());
    }

}