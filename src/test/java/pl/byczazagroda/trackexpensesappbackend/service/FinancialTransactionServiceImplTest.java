package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.controller.FinancialTransactionController;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ApiExceptionBase;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = FinancialTransactionController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {FinancialTransactionRepository.class, FinancialTransactionServiceImpl.class}))
class FinancialTransactionServiceImplTest {
    public static final long ID_1L = 1L;
    public static final long ID_10L = 10L;
    public static final FinancialTransactionType TYPE = FinancialTransactionType.EXPENSE;
    public static final Instant DATE_NOW = Instant.now();

    @MockBean
    private ApiExceptionBase apiExceptionBase;

    @MockBean
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private FinancialTransactionServiceImpl financialTransactionService;

    @MockBean
    private FinancialTransactionModelMapper financialTransactionModelMapper;

    @Test
    @DisplayName("when financial transaction id doesn't exist should not return transaction")
    void shouldNotReturnFinancialTransactionById_WhenIdNotExist() {
        //given
        FinancialTransaction financialTransaction = new FinancialTransaction();
        financialTransaction.setId(ID_1L);
        financialTransaction.setFinancialTransactionType(TYPE);
        financialTransaction.setTransactionDate(DATE_NOW);

        //when
        given(financialTransactionRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> financialTransactionService.findById(ID_10L)).isInstanceOf(AppRuntimeException.class);
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(() ->
                financialTransactionService.findById(ID_10L)).withMessage(ErrorCode.FT01.getBusinessMessage());

    }

    @Test
    @DisplayName("when finding with proper financial transaction id should successfully find transaction")
    void shouldSuccessfullyFindFinancialTransaction_WhenFindingWithProperTransactionId() {
        //given
        FinancialTransaction financialTransaction = new FinancialTransaction();
        financialTransaction.setId(ID_1L);
        financialTransaction.setFinancialTransactionType(TYPE);
        financialTransaction.setTransactionDate(DATE_NOW);

        FinancialTransactionDTO financialTransactionDTO =
                new FinancialTransactionDTO(ID_1L, BigDecimal.valueOf(20), "description", TYPE, DATE_NOW);

        //when
        when(financialTransactionRepository.findById(ID_1L)).thenReturn(Optional.of(financialTransaction));
        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction))
                .thenReturn(financialTransactionDTO);
        FinancialTransactionDTO foundTransaction = financialTransactionService.findById(ID_1L);

        //then
        Assertions.assertEquals(financialTransactionDTO, foundTransaction);
    }
}