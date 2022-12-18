package pl.byczazagroda.trackexpensesappbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ApiExceptionBase;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;
import pl.byczazagroda.trackexpensesappbackend.service.WalletServiceImpl;

import java.time.Instant;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WalletModelMapper.class, ApiExceptionBase.class}))
@ActiveProfiles("test")
class WalletUpdateControllerTest {

    private static final Long ID_0L = 0L;

    private static final Long ID_1L = 1L;

    private static final String NAME_1 = "name one";

    public static final String INVALID_NAME = "@#$%^&";

    public static final String EMPTY_NAME = StringUtils.EMPTY;

    private static final Instant DATE_NOW = Instant.now();

    public static final String TOO_LONG_NAME_MORE_THAN_20_LETTERS = "Too long name - more than 20 letters.";

    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("when update wallet data are correct should return response status OK")
    void shouldReturnResponseStatusOK_WhenUpdateWalletDataAreCorrect() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(ID_1L, NAME_1);
        given(walletService.updateWallet(Mockito.any()))
                .willReturn(new WalletDTO(updDTO.id(), updDTO.name(), DATE_NOW));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(updDTO.id().intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(updDTO.name())));
    }

    @Test
    @DisplayName("when wallet name is empty should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletNameIsEmpty() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(ID_1L, EMPTY_NAME);
        given(walletService.updateWallet(updDTO))
                .willReturn(new WalletDTO(ID_1L, EMPTY_NAME, DATE_NOW));

        //when
        ResultActions result = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name is changed return response status OK and correct response body")
    void shouldReturnStatusOKAndCorrectResponseBody_WhenWalletNameIsChanged() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(ID_1L, NAME_1);
        given(walletService.updateWallet(Mockito.any()))
                .willReturn(new WalletDTO(updDTO.id(), updDTO.name(), DATE_NOW));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(updDTO.id().intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(updDTO.name())));
    }

    @Test
    @DisplayName("when wallet name is too long should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletNameIsTooLong() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(ID_1L, TOO_LONG_NAME_MORE_THAN_20_LETTERS);
        given(walletService.updateWallet(updDTO))
                .willReturn(new WalletDTO(ID_1L, EMPTY_NAME, DATE_NOW));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name contains illegal letters should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletNameContainsIllegalLetters() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(ID_1L, INVALID_NAME);
        given(walletService.updateWallet(updDTO))
                .willReturn(new WalletDTO(ID_1L, EMPTY_NAME, DATE_NOW));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet id is null should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletIdIsNull() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(null, INVALID_NAME);
        given(walletService.updateWallet(updDTO))
                .willReturn(new WalletDTO(null, EMPTY_NAME, DATE_NOW));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet id is zero should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletIdIsZero() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(ID_0L, INVALID_NAME);
        given(walletService.updateWallet(updDTO)).willReturn(new WalletDTO(ID_0L, EMPTY_NAME, DATE_NOW));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet id is negative should return response status bad request")
    void shouldReturnResponseStatusBadRequestWhenWalletIdIsNegative() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(-ID_1L, INVALID_NAME);
        given(walletService.updateWallet(updDTO))
                .willReturn(new WalletDTO(-ID_1L, EMPTY_NAME, DATE_NOW));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name is empty should return response status bad request")
    void shouldReturnResponseStatusBadRequestWhenWalletNameIsEmpty() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(1L, EMPTY_NAME);
        given(walletService.updateWallet(updDTO))
                .willReturn(new WalletDTO(1L, EMPTY_NAME, DATE_NOW));

        //when
        ResultActions result = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name is too long should return response status bad request")
    void shouldReturnResponseStatusBadRequestWhenWalletNameIsTooLong() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(1L, TOO_LONG_NAME_MORE_THAN_20_LETTERS);
        given(walletService.updateWallet(updDTO)).willReturn(new WalletDTO(1L, EMPTY_NAME, DATE_NOW));

        // when
        ResultActions result = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name contains illegal letters should return response status bad request")
    void shouldReturnResponseStatusBadRequestWhenWalletNameContainsIllegalLetters() throws Exception {
        // given
        UpdateWalletDTO updDTO = new UpdateWalletDTO(1L, INVALID_NAME);
        given(walletService.updateWallet(updDTO))
                .willThrow(new RuntimeException("Wallet name contains illegal letters"));

        // when
        ResultActions result = mockMvc.perform(put("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO))));

        // then
        result.andExpect(status().isBadRequest());
    }
}
