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
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import java.math.BigDecimal;
import java.time.Instant;

class FindTransactionByIDIT extends BaseIntegrationTestIT {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void clearDatabase() {
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should return proper financial transaction when search Id exist in database")
    @Test
    void testGetFinancialTransactionById_whenFindingTransactionWithExistingId_thenReturnFinancialTransactionWithCorrespondingId() throws Exception {
        Wallet testWallet = createTestWallet();
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(testWallet, "Test1");
        User user = testWallet.getUser();
        String accessToken = userService.createAccessToken(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/{id}", testFinancialTransaction.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFinancialTransaction))
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpectAll(
               MockMvcResultMatchers.status().isOk(),
        MockMvcResultMatchers.jsonPath("$.id").value(testFinancialTransaction.getId()),
        MockMvcResultMatchers.jsonPath("$.amount").value(testFinancialTransaction.getAmount()),
        MockMvcResultMatchers.jsonPath("$.description").value(testFinancialTransaction.getDescription()),
        MockMvcResultMatchers.jsonPath("$.type").value(testFinancialTransaction.getType().toString()));

        Assertions.assertEquals(1, financialTransactionRepository.count());
    }

    @DisplayName("Should return status NOT_FOUND when search Id does not exist in database")
    @Test
    void testGetFinancialTransactionById_whenSearchIdDoesNotExistInDatabase_thenReturnErrorNotFound() throws Exception {

        Wallet testWallet = createTestWallet();
        User user = testWallet.getUser();
        String accessToken = userService.createAccessToken(user);
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(testWallet, "Test1");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/{id}", 999)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFinancialTransaction))
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));


        resultActions.andExpectAll(MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.FT001.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.FT001.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.FT001.getBusinessStatusCode()));

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

    private User createTestUser() {
        final User userOne = User.builder()
                .id(1L)
                .userName("UserOne")
                .email("user@server.domain.com")
                .password("Password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();

        return userRepository.save(userOne);
    }

    private Wallet createTestWallet() {

        User testUser = createTestUser();
        final Wallet testWallet = Wallet.builder()
                .user(testUser)
                .creationDate(Instant.now())
                .name("TestWallet")
                .build();
        return walletRepository.save(testWallet);
    }
}
