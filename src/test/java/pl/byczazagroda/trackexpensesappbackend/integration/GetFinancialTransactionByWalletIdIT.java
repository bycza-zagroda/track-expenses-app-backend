package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class GetFinancialTransactionByWalletIdIT extends BaseIntegrationTestIT {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @BeforeEach
    void clearTestDB() {
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("when wallet id is correct returns List of financial transactions DTO related to wallet")
    void givenValidWalletId_whenGetFinancialTransactionsByWalletId_thenCorrectResponse() throws Exception {
        // given
        User user = userRepository.save(TestUtils.createUserForTest());
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        String accessToken = userService.createAccessToken(user);

        FinancialTransaction financialTransaction = createTestFinancialTransaction(wallet);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                .queryParam("walletId", String.valueOf(wallet.getId())));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                jsonPath("$.[0].id").value(financialTransaction.getId()),
                jsonPath("$.[0].amount").value(financialTransaction.getAmount()),
                jsonPath("$.[0].description").value(financialTransaction.getDescription()),
                jsonPath("$.[0].type").value(financialTransaction.getType().name()),
                jsonPath("$.[0].date").value(financialTransaction.getDate().toString())
        );

        Assertions.assertEquals(1, financialTransactionRepository.count());
        Assertions.assertEquals(1, walletRepository.count());
    }

    @Test
    @DisplayName("when wallet id is incorrect returns error response dto and has 404 status code")
    void givenInvalidWalletId_whenGetFinancialTransactionsByWalletId_thenNotFoundStatusCode() throws Exception {
        // given
        User user = userRepository.save(TestUtils.createUserForTest());
        String accessToken = userService.createAccessToken(user);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                .queryParam("walletId", "1"));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                jsonPath("$.status").value(ErrorCode.W003.getBusinessStatus()),
                jsonPath("$.message").value(ErrorCode.W003.getBusinessMessage()),
                jsonPath("$.statusCode").value(ErrorCode.W003.getBusinessStatusCode()));

        Assertions.assertEquals(0, financialTransactionRepository.count());
        Assertions.assertEquals(0, walletRepository.count());
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
