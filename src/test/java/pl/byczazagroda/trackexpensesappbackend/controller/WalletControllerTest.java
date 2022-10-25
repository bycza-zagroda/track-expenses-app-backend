package pl.byczazagroda.trackexpensesappbackend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;
import pl.byczazagroda.trackexpensesappbackend.service.WalletServiceImpl;

import java.time.Instant;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                WalletModelMapper.class}))
class WalletControllerTest {

    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void itShouldReturnStatusOKAndCorrectResponseBody() throws Exception {
        //GIVEN
        Instant timeCreated = Instant.now();

        UpdateWalletDTO updateWalletDto = new UpdateWalletDTO(1L, "anyName");
        given(walletService.updateWallet(Mockito.any())).willReturn(
                new WalletDTO(
                        updateWalletDto.id(),
                        updateWalletDto.name(),
                        timeCreated
                )
        );

        //WHEN
        ResultActions editResultActions = mockMvc.perform(
                put("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateWalletDto)))
        );

        //THEN
        editResultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(updateWalletDto.id().intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(updateWalletDto.name())));
    }

    @Test
    void itShouldReturnBadRequestWhenNameIsEmpty() throws Exception {
        //given
        Instant timeCreated = Instant.now();
        UpdateWalletDTO updateWalletDTO = new UpdateWalletDTO(1L, "");
        given(walletService.updateWallet(updateWalletDTO))
                .willReturn(new WalletDTO(1L, "", timeCreated));

        //when
        ResultActions result = mockMvc.perform(
                put("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateWalletDTO)))
        );

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void itShouldReturnBadRequestWhenNameIsTooLong() throws Exception {
        //given
        Instant timeCreated = Instant.now();

        UpdateWalletDTO updateWalletDTO = new UpdateWalletDTO(1L, "Too long name - more than 20 letters.");
        given(walletService.updateWallet(updateWalletDTO))
                .willReturn(new WalletDTO(1L, "", timeCreated));

        //when
        ResultActions result = mockMvc.perform(
                put("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateWalletDTO)))
        );

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsEmpty() throws Exception {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO("");
        given(walletService.createWallet(createWalletDTO))
                .willReturn(new WalletDTO(1L, "", Instant.now()));

        // when
        ResultActions result = mockMvc.perform(post("/api/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(createWalletDTO))));
        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void itShouldReturnBadRequestWhenNameContainsIllegalLetters() throws Exception {
        //given
        Instant timeCreated = Instant.now();

        UpdateWalletDTO updateWalletDTO = new UpdateWalletDTO(1L, "@#$%^&");
        given(walletService.updateWallet(updateWalletDTO))
                .willReturn(new WalletDTO(1L, "", timeCreated));

        //when
        ResultActions result = mockMvc.perform(
                put("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateWalletDTO)))
        );

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsNull() throws Exception {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(null);
        given(walletService.createWallet(createWalletDTO))
                .willReturn(new WalletDTO(1L, null, Instant.now()));

        // when
        ResultActions result = mockMvc.perform(post("/api/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(createWalletDTO))));
        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void itShouldReturnBadRequestWhenIdIsNull() throws Exception {
        //given
        Instant timeCreated = Instant.now();

        UpdateWalletDTO updateWalletDTO = new UpdateWalletDTO(null, "@#$%^&");
        given(walletService.updateWallet(updateWalletDTO))
                .willReturn(new WalletDTO(null, "", timeCreated));

        //when
        ResultActions result = mockMvc.perform(
                put("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateWalletDTO)))
        );

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsTooLong() throws Exception {
        // given
        String walletName = "This wallet name is too long, it contains over 20 characters";
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(walletName);
        given(walletService.createWallet(createWalletDTO))
                .willReturn(
                        new WalletDTO(1L, walletName, Instant.now()));

        // when
        ResultActions result = mockMvc.perform(post("/api/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(createWalletDTO))));
        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void itShouldReturnBadRequestWhenIdIsZero() throws Exception {
        //given
        Instant timeCreated = Instant.now();

        UpdateWalletDTO updateWalletDTO = new UpdateWalletDTO(0L, "@#$%^&");
        given(walletService.updateWallet(updateWalletDTO))
                .willReturn(new WalletDTO(0L, "", timeCreated));

        //when
        ResultActions result = mockMvc.perform(
                put("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateWalletDTO)))
        );

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void itShouldReturnBadRequestWhenIdIsNegative() throws Exception {
        //given
        Instant timeCreated = Instant.now();

        UpdateWalletDTO updateWalletDTO = new UpdateWalletDTO(-1L, "@#$%^&");
        given(walletService.updateWallet(updateWalletDTO))
                .willReturn(new WalletDTO(-1L, "", timeCreated));

        //when
        ResultActions result = mockMvc.perform(
                put("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateWalletDTO)))
        );

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenNameContainsIllegalLetters() throws Exception {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO("#$@");
        given(walletService.createWallet(createWalletDTO))
                .willReturn(new WalletDTO(1L, "#$@", Instant.now()));

        // when
        ResultActions result = mockMvc.perform(post("/api/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(createWalletDTO))));
        // then
        result.andExpect(status().isBadRequest());
    }
}