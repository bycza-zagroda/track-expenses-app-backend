package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class UpdateTransactionByIdIT extends BaseIntegrationTestIT {

    public static final long NOT_EXIST_ID = 100L;
    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @BeforeEach
    void clearDatabase() {
        walletRepository.deleteAll();
        financialTransactionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Update financial transaction with new data provided in DTO when ID found in database")
    @Test
    void updateExistingFinancialTransaction_whenDataProvidedInDTOAndIdIsFoundInDB_thenUpdateExistingFinancialTransactionWithRespectiveId()
            throws Exception {
        // given
        User user = userRepository.save(TestUtils.createTestUser());
        String accessToken = userService.createAccessToken(user);
        Wallet wallet = walletRepository.save(TestUtils.createTestWallet(user));
        FinancialTransaction ft = createTestFinancialTransaction(wallet, user);
        Long categoryId = ft.getFinancialTransactionCategory().getId();

        FinancialTransactionUpdateDTO updateDTO = new FinancialTransactionUpdateDTO(
                new BigDecimal("5.0"),
                Instant.ofEpochSecond(2L),
                "Updated DTO Description",
                FinancialTransactionType.INCOME,
                categoryId);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.
                patch("/api/transactions/{id}", ft.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                jsonPath("$.id").value(ft.getId()),
                jsonPath("$.amount").value(updateDTO.amount()),
                jsonPath("$.date").value(updateDTO.date().toString()),
                jsonPath("$.type").value(updateDTO.type().name()),
                jsonPath("$.categoryId").value(updateDTO.categoryId()),
                jsonPath("$.description").value(updateDTO.description())
        );

        Assertions.assertEquals(1, financialTransactionRepository.count());
        Assertions.assertEquals(1, walletRepository.count());
    }

    @SuppressWarnings("checkstyle:LineLength")
    @DisplayName("Update financial transaction with new data provided in DTO when categoryId and description are null")
    @Test
    void updateExistingFinancialTransactionWithNullCategoryAndDescriptionIdInDTO_whenIdFoundInDB_thenUpdateExistingFinancialTransactionWithRespectiveId() throws Exception {
        // given
        User user = userRepository.save(TestUtils.createTestUser());
        String accessToken = userService.createAccessToken(user);

        Wallet wallet = walletRepository.save(TestUtils.createTestWallet(user));
        FinancialTransaction ft = createTestFinancialTransaction(wallet, user);
        ft.setFinancialTransactionCategory(null);
        ft.setDescription(null);

        FinancialTransactionUpdateDTO updateDTO = new FinancialTransactionUpdateDTO(
                new BigDecimal("5.0"),
                Instant.ofEpochSecond(2L),
                null,
                FinancialTransactionType.INCOME,
                null);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.
                patch("/api/transactions/{id}", ft.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                jsonPath("$.id").value(ft.getId()),
                jsonPath("$.amount").value(updateDTO.amount()),
                jsonPath("$.date").value(updateDTO.date().toString()),
                jsonPath("$.type").value(updateDTO.type().name()),
                jsonPath("$.categoryId").value(updateDTO.categoryId()),
                jsonPath("$.description").value(updateDTO.description())
        );

        Assertions.assertEquals(1, financialTransactionRepository.count());
        Assertions.assertEquals(1, walletRepository.count());
    }

    @DisplayName("Return isNotFound status when ID not found in database")
    @Test
    void testUpdateTransactionById_whenIdIsNotFoundInDB_thenReturnIsNotFound() throws Exception {
        // given
        User user = userRepository.save(TestUtils.createTestUser());
        String accessToken = userService.createAccessToken(user);

        FinancialTransactionUpdateDTO updateDTO = new FinancialTransactionUpdateDTO(
                new BigDecimal("5.0"),
                Instant.ofEpochSecond(2L),
                "Updated DTO Description",
                FinancialTransactionType.EXPENSE,
                null);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.
                patch("/api/transactions/{id}", NOT_EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.FT001.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.FT001.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.FT001.getBusinessStatusCode()
                ));

        Assertions.assertEquals(0, financialTransactionRepository.count());
        Assertions.assertEquals(0, walletRepository.count());
    }

    private FinancialTransactionCategory createTestFinancialTransactionCategory(User user) {
        return financialTransactionCategoryRepository.save(FinancialTransactionCategory.builder()
                .name("TestCategory")
                .type(FinancialTransactionType.INCOME)
                .user(user)
                .build());
    }

    private FinancialTransaction createTestFinancialTransaction(Wallet wallet, User user) {
        return financialTransactionRepository.save(FinancialTransaction.builder()
                .wallet(wallet)
                .amount(new BigDecimal("10.0"))
                .date(Instant.ofEpochSecond(1L))
                .type(FinancialTransactionType.INCOME)
                .description("Test description")
                .financialTransactionCategory(createTestFinancialTransactionCategory(user))
                .build());
    }

}
