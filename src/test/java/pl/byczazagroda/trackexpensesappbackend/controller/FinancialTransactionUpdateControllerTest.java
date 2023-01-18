package pl.byczazagroda.trackexpensesappbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateFinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.error.ErrorResponse;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionService;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType.EXPENSE;

@WebMvcTest(controllers = FinancialTransactionController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = FinancialTransactionServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {FinancialTransactionModelMapper.class, ErrorResponse.class}))
@ActiveProfiles("test")
public class FinancialTransactionUpdateControllerTest {

    public static final long ID_1L = 1L;

    public static final BigDecimal AMOUNT_POSITIVE = new BigDecimal("1.10");

    public static final BigDecimal AMOUNT_NEGATIVE = new BigDecimal("-2.20");

    public static final BigDecimal AMOUNT_WITH_WRONG_DECIMAL_PART = new BigDecimal("9.9999999");

    public static final String DESCRIPTION_IS_TOO_LONG = "Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters." +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters";

    private static final String EMPTY_DESCRIPTION = StringUtils.EMPTY;

    public static final String DESCRIPTION = "Fuel";

    @MockBean
    private FinancialTransactionService financialTransactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("when update financial transaction data are correct should return response status OK")
    void shouldReturnResponseStatusOK_WhenUpdateFinancialTransactionDataAreCorrect() throws Exception {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO = new UpdateFinancialTransactionDTO(AMOUNT_POSITIVE, Instant.now(), DESCRIPTION);
        given(financialTransactionService.updateTransaction(ID_1L, updateTransactionDTO))
                .willReturn(new FinancialTransactionDTO(ID_1L, updateTransactionDTO.amount(), updateTransactionDTO.description(), EXPENSE, Instant.now()));

        //when
        ResultActions result = mockMvc.perform(patch("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateTransactionDTO))));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("when amount is negative value should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenAmountIsNegativeValue() throws Exception {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO = new UpdateFinancialTransactionDTO(AMOUNT_NEGATIVE, Instant.now(), DESCRIPTION);
        given(financialTransactionService.updateTransaction(ID_1L, updateTransactionDTO))
                .willReturn(new FinancialTransactionDTO(ID_1L, AMOUNT_NEGATIVE, DESCRIPTION, EXPENSE, Instant.now()));

        //when
        ResultActions result = mockMvc.perform(patch("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateTransactionDTO))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when length of transaction description is too long should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenLengthOfTransactionDescriptionIsTooLong() throws Exception {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO = new UpdateFinancialTransactionDTO(AMOUNT_POSITIVE, Instant.now(), DESCRIPTION_IS_TOO_LONG);
        given(financialTransactionService.updateTransaction(ID_1L, updateTransactionDTO))
                .willReturn(new FinancialTransactionDTO(ID_1L, AMOUNT_POSITIVE, DESCRIPTION_IS_TOO_LONG, EXPENSE, Instant.now()));

        //when
        ResultActions result = mockMvc.perform(patch("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateTransactionDTO))));

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when transaction description is empty should return response status OK")
    void shouldReturnResponseStatusOK_WhenTransactionDescriptionIsEmpty() throws Exception {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO = new UpdateFinancialTransactionDTO(AMOUNT_POSITIVE, Instant.now(), EMPTY_DESCRIPTION);
        given(financialTransactionService.updateTransaction(ID_1L, updateTransactionDTO))
                .willReturn(new FinancialTransactionDTO(ID_1L, AMOUNT_POSITIVE, EMPTY_DESCRIPTION, EXPENSE, Instant.now()));

        //when
        ResultActions result = mockMvc.perform(patch("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateTransactionDTO))));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("when amount after the decimal point is too long should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenAmountAfterDecimalPointIsTooLong() throws Exception {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO = new UpdateFinancialTransactionDTO(AMOUNT_WITH_WRONG_DECIMAL_PART, Instant.now(), DESCRIPTION);
        given(financialTransactionService.updateTransaction(ID_1L, updateTransactionDTO))
                .willReturn(new FinancialTransactionDTO(ID_1L, AMOUNT_WITH_WRONG_DECIMAL_PART, DESCRIPTION, EXPENSE, Instant.now()));

        //when
        ResultActions result = mockMvc.perform(patch("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updateTransactionDTO))));

        //then
        result.andExpect(status().isBadRequest());
    }
}
