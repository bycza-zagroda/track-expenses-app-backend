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
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class CreateFinancialTransactionIT extends BaseIntegrationTestIT {

    /**
     * The maximum value for the `amount` parameter is described as:
     * {@link  pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCreateDTO  FinancialTransactionCreateDTO}
     */
    private static final BigDecimal MAX_ALLOWED_TRANSACTION_AMOUNT = new BigDecimal("12345678901234.99");

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
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should successfully create financial transaction")
    @Test
    void testCreateFinancialTransaction_whenProvidedCorrectData_thenShouldSaveFinancialTransactionInDatabase()
            throws Exception {
        // given
        User user = createTestUser();
        String accessToken = userService.createAccessToken(user);

        Wallet savedWallet = createTestWallet(user);
        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                savedWallet.getId(),
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
    void testCreateFinancialTransaction_whenCreatingFinancialTransactionIdWalletNotFound_thenReturnIsNotFoundAndErrorMessage() throws Exception {
        // given
        User user = createTestUser();
        String accessToken = userService.createAccessToken(user);

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
        User user = createTestUser();
        String accessToken = userService.createAccessToken(user);

        Wallet savedWallet = createTestWallet(user);
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
        User user = createTestUser();
        String accessToken = userService.createAccessToken(user);

        Wallet savedWallet = createTestWallet(user);
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

    private User createTestUser() {
        final User user = User.builder()
                .userName("userOne")
                .email("email@server.com")
                .password("Password1!")
                .userStatus(UserStatus.VERIFIED)
                .build();
        return userRepository.save(user);
    }

    private Wallet createTestWallet(User user) {
        final Wallet testWallet = Wallet.builder()
                .user(user)
                .creationDate(Instant.now())
                .name("TestWallet")
                .build();
        return walletRepository.save(testWallet);
    }

    private FinancialTransactionCategory createTestFinancialTransactionCategory() {
        final FinancialTransactionCategory testFinancialTransactionCategory = FinancialTransactionCategory.builder()
                .name("name")
                .type(FinancialTransactionType.INCOME)
                .user(createTestUser())
                .build();
        return financialTransactionCategoryRepository.save(testFinancialTransactionCategory);
    }

}
