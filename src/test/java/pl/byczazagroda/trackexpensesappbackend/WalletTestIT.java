package pl.byczazagroda.trackexpensesappbackend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletTestIT extends BaseControllerTestIT {
    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    public void cleanTable() {
        walletRepository.deleteAll();
    }

    @Test
    @DisplayName("It should create wallet")
    void shouldCreateWallet() throws Exception {

        CreateWalletDTO createWalletDTO = new CreateWalletDTO("Test name");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createWalletDTO)))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, walletRepository.findAll().size());
    }
}