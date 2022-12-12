package pl.byczazagroda.trackexpensesappbackend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;
import pl.byczazagroda.trackexpensesappbackend.service.WalletServiceImpl;

import java.time.Instant;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WalletModelMapper.class}))
@ActiveProfiles("test")
class WalletDeleteControllerTest {

    private static final Long ID_0L = 0L;

    private static final Long ID_1L = 1L;

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
        WalletDTO walletDTO = new WalletDTO(ID_1L, NAME_1, DATE_NOW);

        //when
        ResultActions result = mockMvc.perform(delete("/api/wallet/{id}", ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(walletDTO))));

        //then
        result.andExpect(status().isOk());
    }


    @Test
    @DisplayName("when wallet id is zero should return response status no content")
    void shouldReturnResponseStatusNoContent_WhenWalletIdIsZero() throws Exception {
        //given
        WalletDTO walletDTO = new WalletDTO(ID_1L, NAME_1, DATE_NOW);
        doThrow(ConstraintViolationException.class).when(walletService).deleteById(ID_0L);

        //when
        ResultActions result = mockMvc.perform(delete("/api/wallet/{id}", ID_0L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(walletDTO))));

        //then
        result.andExpect(status().isNoContent());
    }
}
