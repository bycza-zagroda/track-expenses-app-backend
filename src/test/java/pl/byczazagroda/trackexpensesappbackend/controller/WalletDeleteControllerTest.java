package pl.byczazagroda.trackexpensesappbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.config.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.wallet.WalletController;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.wallet.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.model.User;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletService;
import pl.byczazagroda.trackexpensesappbackend.wallet.impl.WalletServiceImpl;

import java.time.Instant;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {WalletModelMapper.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
class WalletDeleteControllerTest {

    private static final Long ID_0L = 0L;

    private static final Long WALLET_ID_1L = 1L;

    private static final String NAME_1 = "Wallet name";

    private static final Instant DATE_NOW = Instant.now();

    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("when delete wallet correctly should return response status OK")
    void shouldReturnResponseStatusOK_WhenDeleteWalletCorrectly() throws Exception {
        //given
        User user = TestUtils.createUserForTest();
        //when
        ResultActions result = mockMvc.perform(delete("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(user.getId()))));

        //then
        assertAll(
                () -> result.andExpect(status().isOk()),
                () -> Mockito.verify(walletService, Mockito.times(1))
                        .deleteWalletById(WALLET_ID_1L, user.getId())
        );
    }


    @Test
    @DisplayName("when wallet id is zero should return response status no content")
    void shouldReturnResponseStatusNoContent_WhenWalletIdIsZero() throws Exception {
        //given
        User user = TestUtils.createUserForTest();
        WalletDTO walletDTO = new WalletDTO(WALLET_ID_1L, NAME_1, DATE_NOW, user.getId());
        doThrow(ConstraintViolationException.class).when(walletService).deleteWalletById(ID_0L, user.getId());

        //when
        ResultActions result = mockMvc.perform(delete("/api/wallets/{id}", ID_0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(walletDTO)))
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(user.getId()))));

        //then
        result.andExpectAll(
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
