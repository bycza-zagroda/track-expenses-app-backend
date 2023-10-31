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
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthService;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;

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
    private AuthRepository userRepository;

    @Autowired
    private AuthService authService;


    @BeforeEach
    void clearTestDB() {
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should retrieve a list of financial transactions associated with a valid wallet ID")
    void retrieveTransactions_ValidWalletIdGiven_ShouldReturnListOfTransactions() throws Exception {
        // given
        User user = userRepository.save(TestUtils.createUserForTest());
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        String accessToken = authService.createAccessToken(user);

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
    @DisplayName( "Should return 'Not Found' status when trying to retrieve financial transactions with a non-existent wallet ID")
    void retrieveTransactions_NonExistentWalletIdGiven_ShouldReturnStatusNotFound() throws Exception {
        // given
        User user = userRepository.save(TestUtils.createUserForTest());
        String accessToken = authService.createAccessToken(user);

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
