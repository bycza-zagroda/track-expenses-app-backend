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
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateWalletIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    public void clearTestDB() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should update wallet with provided data when ID is found in database")
    @Test
    void updateWallet_ValidIdGiven_ShouldUpdateWallet() throws Exception {
        //given
        User user = userRepository.save(TestUtils.createUserForTest());
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        String accessToken = authService.createAccessToken(user);

        WalletUpdateDTO updatedWallet = new WalletUpdateDTO("UpdatedWallet");

        // when
        ResultActions response = mockMvc.perform(patch("/api/wallets/{id}", wallet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedWallet))
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        // then
        response.andExpectAll(status().isOk(),
                MockMvcResultMatchers.jsonPath("$.name").value(updatedWallet.name()));


        Assertions.assertEquals(1, walletRepository.count());
    }

    @DisplayName("Should return 'Not Found' status when trying to update wallet with an ID that doesn't exist")
    @Test
    void updateWallet_InvalidIdGiven_ShouldReturnStatusNotFound() throws Exception {
        //given
        User user = userRepository.save(TestUtils.createUserForTest());
        final long walletId = 3L;
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        String accessToken = authService.createAccessToken(user);
        WalletUpdateDTO updatedWallet = new WalletUpdateDTO("UpdatedWallet");

        // when
        ResultActions response = mockMvc.perform(patch("/api/wallets/{id}", walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedWallet))
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        // then
        response.andExpect(status().isNotFound());
    }

}


