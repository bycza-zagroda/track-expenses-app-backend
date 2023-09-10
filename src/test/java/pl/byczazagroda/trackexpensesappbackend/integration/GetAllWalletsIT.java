package pl.byczazagroda.trackexpensesappbackend.integration;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class GetAllWalletsIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void clearTestDB() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("should get all wallets for user which has these wallets")
    void shouldGetAllWalletsAccordingToUserCredentials() throws Exception {
        //given
        List<Wallet> savedWallets = createListTestWallets();
        String accessToken = userService.createAccessToken(savedWallets.get(0).getUser());

        // when
        ResultActions response = mockMvc.perform(get("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedWallets))
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        // then
        response.andExpectAll(status().isOk(),
                MockMvcResultMatchers.jsonPath("$.*", hasSize(2)),
                MockMvcResultMatchers.jsonPath("$.[0].name").value(savedWallets.get(0).getName()),
                MockMvcResultMatchers.jsonPath("$.[0].id").value(savedWallets.get(0).getId()),
                MockMvcResultMatchers.jsonPath("$.[1].name").value(savedWallets.get(1).getName()),
                MockMvcResultMatchers.jsonPath("$.[1].id").value(savedWallets.get(1).getId())
        );
    }

    @Test
    @DisplayName("should get empty wallet list for user who doesn't have wallet")
    void shouldGetEmptyWalletListForUserWhoDoesNotHaveWallet() throws Exception {
        //given
        List<User> listTestUsers = createListTestUsers();
        String accessToken = userService.createAccessToken(listTestUsers.get(2));
        List<Wallet> listTestWallets = createListTestWallets();
        // when
        ResultActions response = mockMvc.perform(get("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(listTestWallets))
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));
        // then
        response.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.*", hasSize(0)));
    }

    private List<User> createListTestUsers() {
        List<User> users;
        final User scroogeMcDuck = User.builder()
                .userName("scroogeMcDuck")
                .email("scroogeMcDuck@wp.pl")
                .password("ScroogeMcDuck09!")
                .userStatus(UserStatus.VERIFIED)
                .build();

        final User heuyDuck = User.builder()
                .userName("heuyDuck")
                .email("heuy@wp.pl")
                .password("HeuyDuck08!")
                .userStatus(UserStatus.VERIFIED)
                .build();

        final User deweyDuck = User.builder()
                .userName("deweyDuck")
                .email("dewey@gmail.com")
                .password("DeweyDuck07!")
                .userStatus(UserStatus.VERIFIED)
                .build();

        users = List.of(scroogeMcDuck, heuyDuck, deweyDuck);
        return userRepository.saveAll(users);
    }

    @NotNull
    private List<Wallet> createListTestWallets() {

        List<Wallet> wallets;
        User user = createListTestUsers().get(0);
        User user1 = createListTestUsers().get(1);

        final Wallet firstWallet = Wallet.builder()
                .user(user)
                .creationDate(Instant.now())
                .name("TestWallet0")
                .build();

        final Wallet secondWallet = Wallet.builder()
                .user(user)
                .creationDate(Instant.now())
                .name("TestWallet2")
                .build();

        final Wallet thirdWallet = Wallet.builder()
                .user(user1)
                .creationDate(Instant.now())
                .name("TestWallet3")
                .build();

        final Wallet forthWallet = Wallet.builder()
                .user(user1)
                .creationDate(Instant.now())
                .name("TestWallet4")
                .build();
        wallets = List.of(firstWallet, secondWallet, thirdWallet, forthWallet);
        return walletRepository.saveAll(wallets);
    }

}
