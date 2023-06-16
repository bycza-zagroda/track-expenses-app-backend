package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc(addFilters = false)
class FindWalletByIdIT extends BaseIntegrationTestIT {

        @Autowired
       private WalletRepository walletRepository;

        @Autowired
        private UserRepository userRepository;

        @BeforeEach
        void clearDatabase() {
            walletRepository.deleteAll();
            userRepository.deleteAll();
        }

        @DisplayName("It should return wallet DTO")
        @Test
        void testFindWalletByIdAPI_whenWalletIdIsCorrect_thenReturnWalletDTO() throws Exception {
            Wallet wallet = createTestWallet();
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
    private User createTestUser() {
        final User userOne = User.builder()
                .userName("userone")
                .email("email@wp.pl")
                .password("password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
        return userRepository.save(userOne);
    }

    private Wallet createTestWallet() {
        final Wallet testWallet = Wallet.builder()
                .user(createTestUser())
                .creationDate(Instant.now())
                .name("TestWallet")
                .build();
        return walletRepository.save(testWallet);
    }

}