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
import pl.byczazagroda.trackexpensesappbackend.IntegrationTestUtils;
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

class DeleteTransactionByIdIT extends BaseIntegrationTestIT {


    public static final long NOT_EXIST_ID = 100L;
    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

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

    @DisplayName("Should delete transaction when ID is correct")
    @Test
    void testDeleteFinancialTransactionById_whenDeletedFinancialTransactionWithExistingId_thenReturnDeletedSuccessfully() throws Exception {
        // given
        User user = IntegrationTestUtils.createTestUser(userRepository);
        String accessToken = userService.createAccessToken(user);

        Wallet wallet = IntegrationTestUtils.createTestWallet(walletRepository, user);
        FinancialTransaction ft = createTestFinancialTransaction(wallet, "Test Transaction");

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/transactions/{id}", ft.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        Assertions.assertEquals(0, financialTransactionRepository.count());
    }

    @DisplayName("Should return is Not Found error when ID does not exist in a database")
    @Test
    void testDeleteFinancialTransactionById_whenFinancialTransactionIdIsIncorrect_thenShouldReturnNotFoundError() throws Exception {
        // given
        User user = IntegrationTestUtils.createTestUser(userRepository);
        String accessToken = userService.createAccessToken(user);
        Wallet wallet = IntegrationTestUtils.createTestWallet(walletRepository, user);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/transactions/{id}", NOT_EXIST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    private FinancialTransaction createTestFinancialTransaction(Wallet wallet, String description) {
        return financialTransactionRepository
                .save(FinancialTransaction.builder()
                        .wallet(wallet)
                        .amount(new BigDecimal("5.0"))
                        .date(Instant.ofEpochSecond(1L))
                        .type(FinancialTransactionType.INCOME)
                        .description(description)
                        .build());
    }

}
