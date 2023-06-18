package pl.byczazagroda.trackexpensesappbackend.integration;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class GetAllWalletsIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearTestDB() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("when getting all wallets should return wallets DTOs list and response status OK")
    void shouldResponseStatusOKAndWalletDTOsList() throws Exception {
        //given
        List<Wallet> savedWallets = createListTestWallets();

        // when
        ResultActions response = mockMvc.perform(get("/api/wallets"));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(savedWallets.size())))
                .andExpect(jsonPath("$.[0].name").value(savedWallets.get(0).getName()))
                .andExpect(jsonPath("$.[0].id").value(savedWallets.get(0).getId()))
                .andExpect(jsonPath("$.[1].name").value(savedWallets.get(1).getName()))
                .andExpect(jsonPath("$.[1].id").value(savedWallets.get(1).getId()))
                .andExpect(jsonPath("$.[2].name").value(savedWallets.get(2).getName()))
                .andExpect(jsonPath("$.[2].id").value(savedWallets.get(2).getId()))
                .andExpect(jsonPath("$.[3].name").value(savedWallets.get(3).getName()))
                .andExpect(jsonPath("$.[3].id").value(savedWallets.get(3).getId()));
    }

    @Test
    @DisplayName("when getting all wallets should return no wallets DTOs list and response status OK")
    void shouldResponseStatusOKAndEmptyWalletDTOsList() throws Exception {
        //given

        // when
        ResultActions response = mockMvc.perform(get("/api/wallets"));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    private  List<User> createListTestUsers() {
       List<User> users;
        final User scroogeMcDuck = User.builder()
                .userName("scroogeMcDuck")
                .email("scroogeMcDuck@wp.pl")
                .password("ScroogeMcDuck09!")
                .userStatus(UserStatus.VERIFIED)
                .build();

        final User heuyDuck = User.builder()
                .userName("heuyDuckHeuy")
                .email("heuy@wp.pl")
                .password("HeuyDuck08!")
                .userStatus(UserStatus.VERIFIED)
                .build();

      users =  List.of(scroogeMcDuck, heuyDuck);
        return userRepository.saveAll(users);
    }

    @NotNull
    private List<Wallet> createListTestWallets() {

        List<Wallet> wallets;

        final Wallet firstWallet = Wallet.builder()
                .user(createListTestUsers().get(0))
                .creationDate(Instant.now())
                .name("TestWallet0")
                .build();

        final Wallet secondWallet = Wallet.builder()
                .user(createListTestUsers().get(0))
                .creationDate(Instant.now())
                .name("TestWallet2")
                .build();

        final Wallet thirdWallet = Wallet.builder()
                .user(createListTestUsers().get(1))
                .creationDate(Instant.now())
                .name("TestWallet3")
                .build();

        final Wallet forthWallet = Wallet.builder()
                .user(createListTestUsers().get(1))
                .creationDate(Instant.now())
                .name("TestWallet4")
                .build();
        wallets = List.of(firstWallet, secondWallet, thirdWallet, forthWallet);
        return walletRepository.saveAll(wallets);
    }

}
