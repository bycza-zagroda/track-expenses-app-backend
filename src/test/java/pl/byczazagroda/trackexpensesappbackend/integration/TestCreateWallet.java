package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestCreateWallet extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void clearTestDB() {
        walletRepository.deleteAll();
    }

    @DisplayName("It should create a new wallet and return wallet DTO")
    @Test
    void testCreateWallet_thenReturnWalletDTO() throws Exception {
        // given
        WalletCreateDTO newWallet = new WalletCreateDTO("NewWallet");

        // when
        ResultActions response = mockMvc.perform(post("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newWallet)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newWallet.name()));

        Assertions.assertEquals(1, walletRepository.count());
    }

    @DisplayName("It should return HTTP 400 with detailed error message when creating wallet with invalid name")
    @Test
    void testCreateWallet_withInvalidName_thenReturnBadRequestWithDetailedErrorMessage() throws Exception {
        // given
        WalletCreateDTO newWallet = new WalletCreateDTO("@3H*(G");

        // when
        ResultActions response = mockMvc.perform(post("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newWallet)));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));

        Assertions.assertEquals(0, walletRepository.count());
    }

    @DisplayName("It should return HTTP 400 with detailed error message when creating wallet with too long name")
    @Test
    void testCreateWallet_withTooLongName_thenReturnBadRequestWithDetailedErrorMessage() throws Exception {
        // given
        WalletCreateDTO newWallet = new WalletCreateDTO("nameOfThisWalletIsTooLong");

        // when
        ResultActions response = mockMvc.perform(post("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newWallet)));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));

        Assertions.assertEquals(0, walletRepository.count());
    }

}
