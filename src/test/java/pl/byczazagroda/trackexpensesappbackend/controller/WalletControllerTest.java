package pl.byczazagroda.trackexpensesappbackend.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.byczazagroda.trackexpensesappbackend.BaseControllerTest;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.service.WalletServiceImpl;

import java.time.Instant;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletControllerTest extends BaseControllerTest {

    @Mock
    private WalletServiceImpl walletService;


    @Test
    void shouldThrowAnExceptionWhenNameIsEmpty() throws Exception {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO("");
        given(walletService.createWallet(createWalletDTO))
                .willReturn(
                        new WalletDTO(
                                1L,
                                "",
                                Instant.now()
                        )
                );
        // when
        ResultActions result = mockMvc.perform(post("/api/wallet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(createWalletDTO))));
        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsNull() throws Exception {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(null);
        given(walletService.createWallet(createWalletDTO))
                .willReturn(
                        new WalletDTO(
                                1L,
                                null,
                                Instant.now()
                        )
                );
        // when
        ResultActions result = mockMvc.perform(post("/api/wallet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(createWalletDTO))));
        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsTooLong() throws Exception {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO("kahkjsdhafksahfkahekjycuiciysajkhfdcxu");
        given(walletService.createWallet(createWalletDTO))
                .willReturn(
                        new WalletDTO(
                                1L,
                                "kahkjsdhafksahfkahekjycuiciysajkhfdcxu",
                                Instant.now()
                        )
                );
        // when
        ResultActions result = mockMvc.perform(post("/api/wallet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(createWalletDTO))));
        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenNameContainsIllegalLetters() throws Exception {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO("#$@");
        given(walletService.createWallet(createWalletDTO))
                .willReturn(
                        new WalletDTO(
                                1L,
                                "#$@",
                                Instant.now()
                        )
                );
        // when
        ResultActions result = mockMvc.perform(post("/api/wallet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(createWalletDTO))));
        // then
        result.andExpect(status().isBadRequest());
    }


}