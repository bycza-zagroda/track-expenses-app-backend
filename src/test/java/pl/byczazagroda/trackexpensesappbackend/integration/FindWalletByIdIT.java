package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class FindWalletByIdIT extends BaseIntegrationTestIT {

        @Autowired
        WalletRepository walletRepository;

        @BeforeEach
        void clearDatabase() {
            walletRepository.deleteAll();
        }

        @DisplayName("It should return wallet DTO")
        @Test
        void testFindWalletByIdAPI_whenWalletIdIsCorrect_thenReturnWalletDTO() throws Exception {
            Wallet wallet = walletRepository.save(new Wallet("Test Wallet"));
            mockMvc.perform(get("/api/wallets/{id}", wallet.getId())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(wallet.getId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(wallet.getName()));
            Assertions.assertEquals(1, walletRepository.count());
        }

        @DisplayName("It should return status Not Found and AppRuntimeException")
        @Test
        void testFindWalletByIdAPI_whenWalletIdIsIncorrect_thenReturnErrorResponse() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/wallets/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn();
            Assertions.assertEquals(0, walletRepository.count());
        }

}