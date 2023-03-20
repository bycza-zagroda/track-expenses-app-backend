package pl.byczazagroda.trackexpensesappbackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestUpdateWallet extends BaseIntegrationTestIT {
    @Autowired
    WalletRepository walletRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void clearTestDB() {

        walletRepository.deleteAll();
    }

    @Test
    void testUpdateWallet_whenWalletIdIsCorrect_thenReturnUpdatedWalletDTO() throws Exception {
        //given
        Wallet savedWallet = new Wallet("TestWallet");
        walletRepository.save(savedWallet);

        Wallet updatedWallet = new Wallet("UpdatedWallet");

        // when
        ResultActions response = mockMvc.perform(patch("/api/wallets/{id}", savedWallet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedWallet)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(updatedWallet.getName()));

        Assertions.assertEquals(1, walletRepository.count());
    }

    @Test
    void testUpdateWallet_whenWalletIdIsIncorrect_thenReturnErrorResponse() throws Exception {
        //given
        long walletId = 3L;
        Wallet updatedWallet = new Wallet("UpdatedWallet");

        // when
        ResultActions response = mockMvc.perform(patch("/api/wallets/{id}", walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedWallet)));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

}


