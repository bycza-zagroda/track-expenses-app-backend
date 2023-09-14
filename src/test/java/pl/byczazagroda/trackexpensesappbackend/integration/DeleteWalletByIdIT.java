package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

class DeleteWalletByIdIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

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

    @DisplayName("Should delete wallet from a database and return status 'OK'")
    @Test
    void testDeleteWalletByIdAPI_whenWalletIdIsCorrect_thenShouldReturnAcceptAndDeleteRecord() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet);
        String accessToken = userService.createAccessToken(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/wallets/{id}", wallet.getId())
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertEquals(0, walletRepository.count());
        Assertions.assertEquals(0, financialTransactionRepository.count());
    }

    @DisplayName("Should return is Not Found error when Id does not exist in a database")
    @Test
    void testDeleteWalletById_whenWalletIdIsIncorrect_thenShouldReturnNotFoundError() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        String accessToken = userService.createAccessToken(user);

        final long notExistingWalletId = 999L;
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/wallets/{id}", notExistingWalletId)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpectAll(MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.W003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.W003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.W003.getBusinessStatusCode()));

        Assertions.assertEquals(1, walletRepository.count());
        Assertions.assertEquals(0, financialTransactionRepository.count());
    }

    private FinancialTransaction createTestFinancialTransaction(Wallet wallet) {
        return financialTransactionRepository.save(FinancialTransaction.builder()
                .wallet(wallet)
                .amount(new BigDecimal("2.0"))
                .date(Instant.ofEpochSecond(1L))
                .type(FinancialTransactionType.INCOME)
                .description("Test transaction")
                .build());
    }

}
