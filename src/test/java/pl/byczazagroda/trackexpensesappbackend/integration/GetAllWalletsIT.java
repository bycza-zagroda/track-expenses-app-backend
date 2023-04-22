package pl.byczazagroda.trackexpensesappbackend.integration;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class GetAllWalletsIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    public void clearTestDB() {
        walletRepository.deleteAll();
    }

    @Test
    @DisplayName("when getting all wallets should return wallets DTOs list and response status OK")
    void shouldResponseStatusOKAndWalletDTOsList() throws Exception {
        //given
        List<Wallet> savedWallets = getWalletList();

        walletRepository.saveAll(savedWallets);

        // when
        ResultActions response = mockMvc.perform(get("/api/wallets"));


        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].name").value(savedWallets.get(0).getName()))
                .andExpect(jsonPath("$.[0].id").value(savedWallets.get(0).getId()))
                .andExpect(jsonPath("$.[1].name").value(savedWallets.get(1).getName()))
                .andExpect(jsonPath("$.[1].id").value(savedWallets.get(1).getId()))
                .andExpect(jsonPath("$.[2].name").value(savedWallets.get(2).getName()))
                .andExpect(jsonPath("$.[2].id").value(savedWallets.get(2).getId()))
                .andExpect(jsonPath("$.[3].name").value(savedWallets.get(3).getName()))
                .andExpect(jsonPath("$.[3].id").value(savedWallets.get(3).getId()));

        Assertions.assertEquals(savedWallets.size(), walletRepository.count());
    }

    @Test
    @DisplayName("when getting all wallets should return no wallets DTOs list and response status OK")
    void shouldResponseStatusOKAndEmptyWalletDTOsList() throws Exception {
        //given


        // when
        ResultActions response = mockMvc.perform(get("/api/wallets"));


        // then
        response.andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(0, walletRepository.count());
    }

    @NotNull
    private static List<Wallet> getWalletList() {
        return List.of(
                new Wallet("TestWallet0"),
                new Wallet("TestWallet1"),
                new Wallet("TestWallet2"),
                new Wallet("TestWallet3")
        );
    }
}
