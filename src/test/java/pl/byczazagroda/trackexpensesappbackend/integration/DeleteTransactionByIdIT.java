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
        User user = createTestUser();
        String accessToken = userService.createAccessToken(user);

        Wallet wallet = createTestWallet(user);
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
        User user = createTestUser();
        String accessToken = userService.createAccessToken(user);
        Wallet wallet = createTestWallet(user);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/transactions/{id}", NOT_EXIST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

        Assertions.assertEquals(1, financialTransactionRepository.count());
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

    private User createTestUser() {
        final User userOne = User.builder()
                .userName("userone")
                .email("Email@wp.pl")
                .password("Password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
        return userRepository.save(userOne);
    }

    private Wallet createTestWallet(User user) {
        final Wallet testWallet = Wallet.builder()
                .user(user)
                .creationDate(Instant.now())
                .name("TestWallet")
                .build();
        return walletRepository.save(testWallet);
    }
}
