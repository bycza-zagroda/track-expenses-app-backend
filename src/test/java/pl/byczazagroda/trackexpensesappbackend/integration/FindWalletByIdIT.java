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
import pl.byczazagroda.trackexpensesappbackend.auth.api.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class FindWalletByIdIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthRepository userRepository;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void clearDatabase() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should retrieve the correct wallet when provided with a valid ID")
    @Test
    void retrieveWallet_ValidIdGiven_ShouldReturnCorrespondingWallet() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        String accessToken = authService.createAccessToken(user);
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));

        ResultActions resultActions = mockMvc.perform(get("/api/wallets/{id}", wallet.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.id").value(wallet.getId()),
                MockMvcResultMatchers.jsonPath("$.name").value(wallet.getName()));
        Assertions.assertEquals(1, walletRepository.count());
    }

    @DisplayName("Should return 'Not Found' status when trying to retrieve a wallet with a non-existent ID")
    @Test
    void retrieveWallet_NonExistentIdGiven_ShouldReturnStatusNotFound() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        String accessToken = authService.createAccessToken(user);

        WalletCreateDTO testWalletDto = new WalletCreateDTO("TestWalletName");

        mockMvc.perform(get("/api/wallets/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testWalletDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Assertions.assertEquals(0, walletRepository.count());
    }

    private AuthLoginDTO createAuthLoginDtoTest() {
        return new AuthLoginDTO("email@wp.pl", "Password1@", true);
    }

}
