package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
class UpdateTransactionByIdIT extends BaseIntegrationTestIT {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void clearDatabase() {
        walletRepository.deleteAll();
        financialTransactionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Update financial transaction with new data provided in DTO when Id found in database")
    @Test
    void updateExistingFinancialTransaction_whenDataProvidedInDTOAndIdIsFoundInDB_thenUpdateExistingFinancialTransactionWithRespectiveId() throws Exception {
        Wallet wallet = createTestWallet();
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet);
        Long categoryId = testFinancialTransaction.getFinancialTransactionCategory().getId();

        FinancialTransactionUpdateDTO updateDTO = new FinancialTransactionUpdateDTO(
                new BigDecimal("5.0"),
                Instant.ofEpochSecond(2L),
                "Updated DTO Description",
                FinancialTransactionType.INCOME,
                categoryId);

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
                .andExpect(jsonPath("$.categoryId").value(updateDTO.categoryId()))
                .andExpect(jsonPath("$.description").value(updateDTO.description()));

        Assertions.assertEquals(1, financialTransactionRepository.count());
        Assertions.assertEquals(1, walletRepository.count());
    }
    @DisplayName("Update financial transaction with new data provided in DTO when categoryId and description are null")
    @Test
    void updateExistingFinancialTransactionWithNullCategoryAndDescriptionIdInDTO_whenIdFoundInDB_thenUpdateExistingFinancialTransactionWithRespectiveId() throws Exception {
        Wallet wallet = createTestWallet();
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet);
        testFinancialTransaction.setFinancialTransactionCategory(null);
        testFinancialTransaction.setDescription(null);

        FinancialTransactionUpdateDTO updateDTO = new FinancialTransactionUpdateDTO(
                new BigDecimal("5.0"),
                Instant.ofEpochSecond(2L),
                null,
                FinancialTransactionType.INCOME,
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
                .andExpect(jsonPath("$.categoryId").value(updateDTO.categoryId()))
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

    private FinancialTransactionCategory createTestFinancialTransactionCategory() {
        return financialTransactionCategoryRepository.save(FinancialTransactionCategory.builder()
                .name("TestCategory")
                .type(FinancialTransactionType.INCOME)
                .build());
    }
    private FinancialTransaction createTestFinancialTransaction(Wallet wallet) {
        return financialTransactionRepository.save(FinancialTransaction.builder()
                .wallet(wallet)
                .amount(new BigDecimal("10.0"))
                .date(Instant.ofEpochSecond(1L))
                .type(FinancialTransactionType.INCOME)
                .description("Test description")
                .financialTransactionCategory(createTestFinancialTransactionCategory())
                .build());
    }

    private User createTestUser() {
        final User userOne = User.builder()
                .userName("userone")
                .email("email@wp.pl")
                .password("password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
        return userRepository.save(userOne);
    }

    private Wallet createTestWallet() {
        final Wallet testWallet = Wallet.builder()
                .user(createTestUser())
                .creationDate(Instant.now())
                .name("TestWallet")
                .build();
        return walletRepository.save(testWallet);
    }
}