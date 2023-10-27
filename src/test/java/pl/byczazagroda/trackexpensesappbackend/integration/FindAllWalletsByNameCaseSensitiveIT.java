package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthService;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

class FindAllWalletsByNameCaseSensitiveIT extends BaseIntegrationTestIT {

    private static final String WALLET_NAME = "wallet";

    private static final String WALLET_NAME_TOO_LONG = "The quick, brown fox jumps over";

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private AuthRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void clearDatabase() {
        walletRepository.deleteAll();
        financialTransactionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should retrieve all wallets matching the provided name, irrespective of case")
    @Test
    void retrieveAllWallets_MatchingNameGiven_ShouldReturnCorrespondingWallets() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        String accessToken = authService.createAccessToken(user);

        List<Wallet> wallets = createListTestWallets(user);
        Wallet wallet1 = wallets.get(0);
        Wallet wallet2 = wallets.get(1);
        Wallet wallet3 = wallets.get(2);
        Wallet wallet4 = wallets.get(4);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/wallets/wallets/{name}", WALLET_NAME)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(wallet1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(wallet2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].id").value(wallet3.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[3].id").value(wallet4.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(wallet1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value(wallet2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].name").value(wallet3.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[3].name").value(wallet4.getName()));
    }

    @DisplayName("Should return 'Bad Request' status when search name is excessively long")
    @Test
    void retrieveAllWallets_ExcessivelyLongNameGiven_ShouldReturnStatusBadRequest() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        String accessToken = authService.createAccessToken(user);
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/wallets/wallets/{name}", WALLET_NAME_TOO_LONG)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()));

        Assertions.assertEquals(1, walletRepository.count());
    }

    @DisplayName("Should return an empty result when the provided search name has no matches in the database")
    @Test
    void retrieveAllWallets_NonMatchingNameGiven_ShouldReturnEmptyArray() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        String accessToken = authService.createAccessToken(user);
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/wallets/wallets/{name}", "notExistingName")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").doesNotExist());
    }

    private List<Wallet> createListTestWallets(User testUser) {
        List<Wallet> wallets = new ArrayList<>();

        final Wallet firstWallet = Wallet.builder()
                .user(testUser)
                .creationDate(Instant.now())
                .name("WalletTest")
                .build();

        final Wallet secondWallet = Wallet.builder()
                .user(testUser)
                .creationDate(Instant.now())
                .name("wallet")
                .build();

        final Wallet thirdWallet = Wallet.builder()
                .user(testUser)
                .creationDate(Instant.now())
                .name("WALLET")
                .build();

        final Wallet forthWallet = Wallet.builder()
                .user(testUser)
                .creationDate(Instant.now())
                .name("Bag")
                .build();
        final Wallet fifthWallet = Wallet.builder()
                .user(testUser)
                .creationDate(Instant.now())
                .name("Wallet Test")
                .build();

        wallets.add(firstWallet);
        wallets.add(secondWallet);
        wallets.add(thirdWallet);
        wallets.add(forthWallet);
        wallets.add(fifthWallet);

        return walletRepository.saveAll(wallets);
    }

}
