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
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.api.dto.FinancialTransactionCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.auth.userModel.User;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthService;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class CreateFinancialTransactionIT extends BaseIntegrationTestIT {

    /**
     * The maximum value for the `amount` parameter is described as:
     * {@link  FinancialTransactionCreateDTO  FinancialTransactionCreateDTO}
     */
    private static final BigDecimal MAX_ALLOWED_TRANSACTION_AMOUNT = new BigDecimal("12345678901234.99");

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthRepository userRepository;

    @Autowired
    private AuthService authService;


    @BeforeEach
    void clearDatabase() {
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should successfully create financial transaction")
    @Test
    void testCreateFinancialTransaction_whenProvidedCorrectData_thenShouldSaveFinancialTransactionInDatabase()
            throws Exception {
        // given
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);

        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));

        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                wallet.getId(),
                new BigDecimal("5.0"),
                "Description test",
                Instant.ofEpochSecond(1L),
                FinancialTransactionType.EXPENSE,
                null);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(financialTransactionCreateDTO))
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isCreated(),
                jsonPath("$.amount").value(financialTransactionCreateDTO.amount()),
                jsonPath("$.type").value(financialTransactionCreateDTO.type().toString()),
                jsonPath("$.description").value(financialTransactionCreateDTO.description()
                ));

        Assertions.assertEquals(1, walletRepository.count());
        Assertions.assertEquals(1, financialTransactionRepository.count());
    }

    @DisplayName("Should return Wallet Not Found message when creating financial transaction wallet Id that doesnt exist in database")
    @Test
    void testCreateFinancialTransaction_whenCreatingFinancialTransactionIdWalletNotFound_thenReturnIsNotFoundAndErrorMessage()
            throws Exception {
        // given
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);

        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                1L,
                new BigDecimal("5.0"),
                "Description test",
                Instant.ofEpochSecond(1L),
                FinancialTransactionType.EXPENSE,
                null);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(financialTransactionCreateDTO))
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.W003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.W003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.W003.getBusinessStatusCode())
        );

        Assertions.assertEquals(0, financialTransactionRepository.count());
    }

    @DisplayName("Should return bad request and validation failed error when creating financial transaction with amount exceeding limit")
    @Test
    void testCreateFinancialTransaction_whenAmountExceedsLimit_thenReturnBadRequestAndErrorValidationFailed() throws Exception {
        // given
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);

        Wallet savedWallet = walletRepository.save(TestUtils.createWalletForTest(user));

        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                savedWallet.getId(),
                MAX_ALLOWED_TRANSACTION_AMOUNT,
                "Test Description",
                Instant.ofEpochSecond(1L),
                FinancialTransactionType.EXPENSE,
                null);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(financialTransactionCreateDTO))
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));

        Assertions.assertEquals(1, walletRepository.count());
        Assertions.assertEquals(0, financialTransactionRepository.count());
    }

    @DisplayName("When financial transaction type does not match with category type should throw exception")
    @Test
    void testCreateFinancialTransaction_whenFinancialTransactionTypeNotMatchWithCategoryType_thenThrowException() throws Exception {
        // given
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);

        Wallet savedWallet = walletRepository.save(TestUtils.createWalletForTest(user));

        FinancialTransactionCategory ftCategory = createTestFinancialTransactionCategory(user);
        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                savedWallet.getId(),
                new BigDecimal("10"),
                "Test Description",
                Instant.ofEpochSecond(1L),
                FinancialTransactionType.EXPENSE,
                ftCategory.getId());


        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(financialTransactionCreateDTO))
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.FT002.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.FT002.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.FT002.getBusinessStatusCode()));

        Assertions.assertEquals(1, walletRepository.count());
        Assertions.assertEquals(0, financialTransactionRepository.count());
        Assertions.assertEquals(1, financialTransactionCategoryRepository.count());
    }

    private FinancialTransactionCategory createTestFinancialTransactionCategory(User user) {
        final FinancialTransactionCategory testFinancialTransactionCategory = FinancialTransactionCategory.builder()
                .name("name")
                .type(FinancialTransactionType.INCOME)
                .user(user)
                .build();
        return financialTransactionCategoryRepository.save(testFinancialTransactionCategory);
    }

}
