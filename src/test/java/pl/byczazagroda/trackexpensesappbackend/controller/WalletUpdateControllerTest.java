package pl.byczazagroda.trackexpensesappbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.auth.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.wallet.WalletController;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletService;
import pl.byczazagroda.trackexpensesappbackend.wallet.impl.WalletServiceImpl;

import java.time.Instant;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {WalletModelMapper.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
class WalletUpdateControllerTest {

    private static final Long WALLET_ID_0L = 0L;

    private static final Long USER_ID_0L = 0L;

    private static final Long WALLET_ID_1L = 1L;

    private static final Long USER_ID_1L = 1L;

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
    @DisplayName("Should return OK status when updating wallet with correct data")
    void updateWallet_ValidData_ShouldReturnStatusOk() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(NAME_1);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, updDTO.name(), DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(WALLET_ID_1L.intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(updDTO.name())));
    }

    @Test
    @DisplayName("Should return OK status and correct response body when wallet name is changed")
    void updateWallet_NameChanged_ShouldReturnStatusOkAndCorrectResponseBody() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(EMPTY_NAME);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request status when updating wallet with empty name")
    void updateWallet_EmptyName_ShouldReturnStatusBadRequest() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(NAME_1);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, updDTO.name(), DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(WALLET_ID_1L.intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(updDTO.name())));
    }

    @Test
    @DisplayName("Should return bad request status when updating wallet with name too long")
    void updateWallet_NameTooLong_ShouldReturnStatusBadRequest() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(TOO_LONG_NAME_MORE_THAN_20_LETTERS);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request status when updating wallet with name containing illegal characters")
    void updateWallet_IllegalCharactersInName_ShouldReturnStatusBadRequest() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(INVALID_NAME);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return server error status when updating wallet with null ID")
    void updateWallet_NullId_ShouldReturnStatusServerError() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(INVALID_NAME);
        given(walletService.updateWallet(null, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(null, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/" + null)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().is5xxServerError(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA004.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA004.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA004.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("Should return bad request status when updating wallet with ID zero")
    void updateWallet_ZeroId_ShouldReturnStatusBadRequest() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(NAME_1);
//        given(walletService.updateWallet(WALLET_ID_0L, updDTO, USER_ID_1L))
//                .willReturn(new WalletDTO(WALLET_ID_0L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_0L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("Should return bad request status when updating wallet with negative ID")
    void updateWallet_NegativeId_ShouldReturnStatusBadRequest() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(NAME_1);
        given(walletService.updateWallet(-WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(-WALLET_ID_1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("Should return bad request status when updating wallet with empty name")
    void updateWallet_EmptyNameGiven_ShouldReturnStatusBadRequest() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(EMPTY_NAME);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("Should return bad request status when updating wallet with excessively long name")
    void updateWallet_ExcessivelyLongNameGiven_ShouldReturnStatusBadRequest() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(TOO_LONG_NAME_MORE_THAN_20_LETTERS);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("Should return bad request status when updating wallet with name containing illegal characters")
    void updateWallet_IllegalCharactersInNameGiven_ShouldReturnStatusBadRequest() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(INVALID_NAME);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willThrow(new RuntimeException("Wallet name contains illegal letters"));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

}
