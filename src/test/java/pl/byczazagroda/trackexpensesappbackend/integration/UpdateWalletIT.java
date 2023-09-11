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
import pl.byczazagroda.trackexpensesappbackend.IntegrationTestUtils;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateWalletIT extends BaseIntegrationTestIT {

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

    @DisplayName("It should return updated wallet DTO when ID is correct")
    @Test
    void testUpdateWallet_whenWalletIdIsCorrect_thenReturnUpdatedWalletDTO() throws Exception {
        //given
        User user = IntegrationTestUtils.createTestUser(userRepository);
        Wallet savedWallet = IntegrationTestUtils.createTestWallet(walletRepository, user);
        String accessToken = userService.createAccessToken(user);

        WalletUpdateDTO updatedWallet = new WalletUpdateDTO("UpdatedWallet");

        // when
        ResultActions response = mockMvc.perform(patch("/api/wallets/{id}", savedWallet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedWallet))
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        // then
        response.andExpectAll(status().isOk(),
                MockMvcResultMatchers.jsonPath("$.name").value(updatedWallet.name()));


        Assertions.assertEquals(1, walletRepository.count());
    }

    @DisplayName("It should return AppRuntimeException with message WALLET_NOT_FOUND when ID is incorrect")
    @Test
    void testUpdateWallet_whenWalletIdIsIncorrect_thenReturnErrorResponse() throws Exception {
        //given
        User user = IntegrationTestUtils.createTestUser(userRepository);
        long walletId = 3L;
        Wallet savedWallet = IntegrationTestUtils.createTestWallet(walletRepository, user);
        String accessToken = userService.createAccessToken(user);
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


