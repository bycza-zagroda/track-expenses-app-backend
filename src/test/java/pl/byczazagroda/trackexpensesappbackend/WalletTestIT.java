package pl.byczazagroda.trackexpensesappbackend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.byczazagroda.trackexpensesappbackend.dto.UserDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletTestIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void cleanTable() {
        walletRepository.deleteAll();
    }

    @Test
    @DisplayName("It should create wallet")
    void shouldCreateWallet() throws Exception {

        WalletCreateDTO walletCreateDTO = new WalletCreateDTO("Test name", createTestUserDTO().id());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletCreateDTO)))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, walletRepository.findAll().size());
    }
    private UserDTO createTestUserDTO() {

        return new UserDTO(2L, "userone", "email@wp.pl", "password1@",
                 UserStatus.VERIFIED);
    }


}
