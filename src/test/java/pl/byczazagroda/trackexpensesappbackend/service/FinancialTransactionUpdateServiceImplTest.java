package pl.byczazagroda.trackexpensesappbackend.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.controller.FinancialTransactionController;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateFinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.error.ErrorResponse;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = FinancialTransactionController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {FinancialTransactionRepository.class, FinancialTransactionServiceImpl.class}))
class FinancialTransactionUpdateServiceImplTest {

    public static final long ID_1L = 1L;

    public static final long ID_LOWER_THEN_1 = 0L;

    public static final BigDecimal AMOUNT_POSITIVE = new BigDecimal(110.595959);

    public static final BigDecimal AMOUNT_NEGATIVE = new BigDecimal(-99.01);

    public static final FinancialTransactionType TYPE = FinancialTransactionType.EXPENSE;

    private static final Instant DATE_NOW = Instant.now();

    public static final String DESCRIPTION_1 = "Fuel";

    public static final String DESCRIPTION_2 = "Utility bills";

    public static final String DESCRIPTION_IS_TOO_LONG = "Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters." +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters" +
            " Name of description is too long - more than 255 letters";

    private static final String EMPTY_DESCRIPTION = StringUtils.EMPTY;

    @MockBean
    private ErrorResponse errorResponse;

    @MockBean
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private FinancialTransactionServiceImpl financialTransactionService;

    @MockBean
    private FinancialTransactionModelMapper financialTransactionModelMapper;

    @Test
    @DisplayName("when finding financial transaction by id should update transaction")
    void shouldUpdateFinancialTransaction_whenFindTransactionById() {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO =
                new UpdateFinancialTransactionDTO(AMOUNT_POSITIVE, DATE_NOW, DESCRIPTION_2);
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setId(ID_1L);
        transaction.setAmount(AMOUNT_POSITIVE);
        transaction.setDescription(DESCRIPTION_1);
        transaction.setFinancialTransactionType(TYPE);
        transaction.setTransactionDate(DATE_NOW);
        FinancialTransactionDTO newTransactionDTO =
                new FinancialTransactionDTO(ID_1L, AMOUNT_POSITIVE, DESCRIPTION_2, TYPE, DATE_NOW);

        //when
        when(financialTransactionRepository.findById(ID_1L))
                .thenReturn(Optional.of(transaction));
        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(transaction))
                .thenReturn(newTransactionDTO);
        FinancialTransactionDTO transactionDTO =
                financialTransactionService.updateTransaction(ID_1L, updateTransactionDTO);

        //then
        assertThat(transactionDTO.description()).isEqualTo(updateTransactionDTO.description());
    }

    @Test
    @DisplayName("when the identifier value is lower than one should not return transaction")
    void shouldNotReturnFinancialTransactionById_WhenIdIsLowerThenOne() {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO =
                new UpdateFinancialTransactionDTO(AMOUNT_POSITIVE, DATE_NOW, DESCRIPTION_1);

        //when

        //then
        Assertions.assertThrows(ConstraintViolationException.class,
                () -> financialTransactionService.updateTransaction(ID_LOWER_THEN_1, updateTransactionDTO));
    }

    @Test
    @DisplayName("when the transaction amount is a negative value should not updated transaction")
    void shouldNotUpdateTransaction_WhenTransactionAmountValueIsNegative() {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO =
                new UpdateFinancialTransactionDTO(AMOUNT_NEGATIVE, DATE_NOW, DESCRIPTION_1);

        //when

        //then
        Assertions.assertThrows(ConstraintViolationException.class,
                () -> financialTransactionService.updateTransaction(ID_1L, updateTransactionDTO));
    }

    @Test
    @DisplayName("when transaction description is too long should not update transaction")
    void shouldNotUpdateTransaction_WhenTransactionDescriptionIsTooLong() {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO =
                new UpdateFinancialTransactionDTO(AMOUNT_POSITIVE, DATE_NOW, DESCRIPTION_IS_TOO_LONG);

        //when

        //then
        Assertions.assertThrows(ConstraintViolationException.class,
                () -> financialTransactionService.updateTransaction(ID_1L, updateTransactionDTO));
    }

    @Test
    @DisplayName("when transaction description is empty should update transaction")
    void shouldUpdateFinancialTransaction_WhenTransactionDescriptionIsEmpty() {
        //given
        UpdateFinancialTransactionDTO updateTransactionDTO =
                new UpdateFinancialTransactionDTO(AMOUNT_POSITIVE, DATE_NOW, EMPTY_DESCRIPTION);
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setId(ID_1L);
        transaction.setAmount(AMOUNT_POSITIVE);
        transaction.setDescription(DESCRIPTION_1);
        transaction.setFinancialTransactionType(TYPE);
        transaction.setTransactionDate(DATE_NOW);
        FinancialTransactionDTO newTransactionDTO =
                new FinancialTransactionDTO(ID_1L, AMOUNT_POSITIVE, EMPTY_DESCRIPTION, TYPE, DATE_NOW);

        //when
        when(financialTransactionRepository.findById(ID_1L))
                .thenReturn(Optional.of(transaction));
        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(transaction))
                .thenReturn(newTransactionDTO);
        FinancialTransactionDTO transactionDTO =
                financialTransactionService.updateTransaction(ID_1L, updateTransactionDTO);

        //then
        assertThat(transactionDTO.description()).isEqualTo(updateTransactionDTO.description());
    }
}
