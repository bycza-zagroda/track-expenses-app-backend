package pl.byczazagroda.trackexpensesappbackend.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.dto.FinancialTransactionCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.dto.FinancialTransactionUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.impl.FinancialTransactionServiceImpl;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.general.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType.EXPENSE;
import static pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType.INCOME;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FinancialTransactionServiceImplTest {

    public static final long ID_1L = 1L;

    public static final long ID_2L = 2L;

    public static final long ID_10L = 10L;

    public static final Instant DATE_NOW = Instant.now();

    private static final String DESCRIPTION = "Description";

    @Mock
    private ErrorStrategy errorStrategy;

    @Mock
    private FinancialTransactionRepository financialTransactionRepository;

    @InjectMocks
    private FinancialTransactionServiceImpl financialTransactionService;

    @Mock
    private FinancialTransactionModelMapper financialTransactionModelMapper;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;


    @Test
    @DisplayName("Should throw an exception when transaction type doesn't match category type")
    void createFinancialTransaction_TypeMismatchCategory_ThrowAppRuntimeException() {
        //given
        User user = TestUtils.createUserForTest();
        Long userId = user.getId();

        FinancialTransactionCreateDTO ftCreateDTO = createFinancialTransactionCreateDTO();
        Wallet wallet = TestUtils.createWalletForTest(user);

        when(walletRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(wallet));
        FinancialTransaction ft = createEntityFinancialTransaction(ID_1L);
        when(financialTransactionRepository.save(any())).thenReturn(ft);
        FinancialTransactionDTO ftDTO = createFinancialTransactionDTO();
        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(any()))
                .thenReturn(ftDTO);
        FinancialTransactionCategory ftCategory = createFinancialTransactionCategory(INCOME, user);
        when(financialTransactionCategoryRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(ftCategory));

        //when & then
        AppRuntimeException exception = assertThrows(
                AppRuntimeException.class,
                () -> financialTransactionService.createFinancialTransaction(ftCreateDTO, userId));
        assertEquals(ErrorCode.FT002.getBusinessStatusCode(), exception.getBusinessStatusCode());
    }

    @NotNull
    private FinancialTransactionCategory createFinancialTransactionCategory(FinancialTransactionType type, User user) {
        return new FinancialTransactionCategory(ID_1L, "Category name", type, DATE_NOW, null, user);
    }

    @Test
    @DisplayName("Should not create a financial transaction without an existing wallet and throw an exception")
    void createFinancialTransaction_WalletNotFound_ThrowWalletException() {
        //given
        Long userId = TestUtils.createUserForTest().getId();

        FinancialTransactionCreateDTO financialTransactionCreateDTO = createFinancialTransactionCreateDTO();
        when(walletRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

        //when & then
        AppRuntimeException exception = assertThrows(
                AppRuntimeException.class,
                () -> financialTransactionService.createFinancialTransaction(financialTransactionCreateDTO, userId)
        );
        assertEquals(ErrorCode.W003.getBusinessStatusCode(), exception.getBusinessStatusCode());
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create a financial transaction with valid parameters")
    void createFinancialTransaction_ValidParameters_ReturnsFinancialTransactionDTO() {
        //given
        User user = TestUtils.createUserForTest();

        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                ID_1L, ONE, EMPTY, DATE_NOW, EXPENSE, ID_1L);

        Wallet wallet = new Wallet();

        when(walletRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(wallet));
        FinancialTransaction financialTransaction = createEntityFinancialTransaction(ID_1L);
        when(financialTransactionRepository.save(any())).thenReturn(financialTransaction);

        FinancialTransactionDTO financialTransactionDTO = createFinancialTransactionDTO();
        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(any()))
                .thenReturn(financialTransactionDTO);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(EXPENSE, user);
        when(financialTransactionCategoryRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(financialTransactionCategory));

        //when
        FinancialTransactionDTO result = financialTransactionService
                .createFinancialTransaction(financialTransactionCreateDTO, user.getId());

        //then
        assertAll(() -> assertEquals(financialTransactionDTO, result), () -> assertEquals(financialTransactionDTO.id(),
                result.id()));
        verify(financialTransactionRepository, atMostOnce()).save(any());
        verify(walletRepository, atMostOnce()).findById(any());
        verify(financialTransactionModelMapper, atMostOnce())
                .mapFinancialTransactionEntityToFinancialTransactionDTO(any());
    }

    @Test
    @DisplayName("Should create a financial transaction with an empty description")
    void createFinancialTransaction_EmptyDescription_ReturnsFinancialTransaction() {
        //given
        User user = TestUtils.createUserForTest();

        Wallet wallet = TestUtils.createWalletForTest(user);

        FinancialTransactionCreateDTO ftCreateDTO = new FinancialTransactionCreateDTO(
                ID_1L, ONE, EMPTY, DATE_NOW, EXPENSE, ID_1L);

        when(walletRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(wallet));
        FinancialTransaction financialTransaction = createEntityFinancialTransaction(ID_1L);
        financialTransaction.setWallet(wallet);
        financialTransaction.setAmount(ONE);
        financialTransaction.setDescription(EMPTY);

        when(financialTransactionRepository.save(any())).thenReturn(financialTransaction);
        FinancialTransactionDTO financialTransactionDTO =
                new FinancialTransactionDTO(ID_1L, ONE, EMPTY, EXPENSE, DATE_NOW, ID_1L);

        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(any()))
                .thenReturn(financialTransactionDTO);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(EXPENSE, user);
        when(financialTransactionCategoryRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(financialTransactionCategory));

        //when
        FinancialTransactionDTO result = financialTransactionService
                .createFinancialTransaction(ftCreateDTO, user.getId());

        //then
        assertAll(() -> assertEquals(EMPTY, financialTransactionDTO.description()),
                () -> assertEquals(financialTransactionDTO, result),
                () -> assertEquals(financialTransactionDTO.id(), result.id()));
        verify(financialTransactionRepository, atMostOnce()).save(any());
        verify(walletRepository, atMostOnce()).findById(any());
        verify(financialTransactionModelMapper, atMostOnce())
                .mapFinancialTransactionEntityToFinancialTransactionDTO(any());
    }

    @Test
    @DisplayName("Should throw an exception when updating a financial transaction with an invalid ID")
    void updateFinancialTransaction_InvalidId_ThrowAppRuntimeException() {
        //given
        Long userId = TestUtils.createUserForTest().getId();

        FinancialTransactionUpdateDTO updateDTO = createFinancialTransactionUpdateDTO();
        when(financialTransactionRepository.findByIdAndWalletUserId(any(), any())).thenReturn(Optional.empty());

        //when & then
        AppRuntimeException exception = assertThrows(
                AppRuntimeException.class,
                () -> financialTransactionService.updateFinancialTransaction(ID_1L, updateDTO, userId)
        );

        assertEquals(ErrorCode.FT001.getBusinessStatusCode(), exception.getBusinessStatusCode());
        assertEquals(ErrorCode.FT001.getBusinessMessage(), exception.getBusinessMessage());
        verify(financialTransactionRepository, atMostOnce()).findById(any());
        verify(financialTransactionModelMapper, never()).mapFinancialTransactionEntityToFinancialTransactionDTO(any());
    }

    @Test
    @DisplayName("Should update the financial transaction with valid parameters")
    void updateFinancialTransaction_ValidParameters_ReturnsUpdatedTransaction() {
        //given
        User user = TestUtils.createUserForTest();

        FinancialTransactionUpdateDTO ftUpdateDTO = createFinancialTransactionUpdateDTO();

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(EXPENSE, user);
        when(financialTransactionCategoryRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.of(financialTransactionCategory));

        FinancialTransaction financialTransaction = createEntityFinancialTransaction(ID_1L);
        financialTransaction.setDescription(EMPTY);
        financialTransaction.setAmount(ONE);

        FinancialTransactionDTO financialTransactionDTO
                = new FinancialTransactionDTO(ID_1L, TEN, DESCRIPTION, EXPENSE, DATE_NOW, ID_1L);

        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction))
                .thenReturn(financialTransactionDTO);

        when(financialTransactionRepository
                .findByIdAndWalletUserId(ID_1L, user.getId()))
                .thenReturn(Optional.of(financialTransaction));

        //when
        FinancialTransactionDTO result = financialTransactionService.updateFinancialTransaction(ID_1L, ftUpdateDTO, user.getId());

        //then
        assertAll(
                () -> assertEquals(ftUpdateDTO.amount(), result.amount()),
                () -> assertEquals(ftUpdateDTO.description(), result.description()),
                () -> assertEquals(ftUpdateDTO.type(), result.type()),
                () -> assertEquals(ftUpdateDTO.date(), result.date()));

        verify(financialTransactionRepository, atMostOnce()).save(any());
        verify(financialTransactionRepository, atMostOnce()).findById(any());
        verify(financialTransactionModelMapper, atMostOnce())
                .mapFinancialTransactionEntityToFinancialTransactionDTO(any());
    }

    @Test
    @DisplayName("Should return transactions when provided with a valid wallet ID")
    void findFinancialTransactions_ValidWalletId_ReturnsTransactionsList() {
        //given
        User user = TestUtils.createUserForTest();

        FinancialTransaction financialTransaction1 = createEntityFinancialTransaction(ID_1L);
        FinancialTransactionDTO financialTransactionDTO1 = createFinancialTransactionDTO();

        FinancialTransaction financialTransaction2 = createEntityFinancialTransaction(ID_1L);
        financialTransaction2.setId(ID_2L);

        FinancialTransactionDTO financialTransactionDTO2 =
                new FinancialTransactionDTO(ID_2L, BigDecimal.ONE, "desc",
                        FinancialTransactionType.EXPENSE, DATE_NOW, null);

        List<FinancialTransaction> financialTransactionsList = new ArrayList<>();
        financialTransactionsList.add(financialTransaction1);
        financialTransactionsList.add(financialTransaction2);

        Wallet wallet = TestUtils.createWalletForTest(TestUtils.createUserForTest());

        financialTransaction1.setWallet(wallet);
        financialTransaction2.setWallet(wallet);

        wallet.setFinancialTransactionList(financialTransactionsList);

        //when
        when(walletRepository.existsById(ID_1L)).thenReturn(true);
        when(financialTransactionRepository.findAllByWalletIdAndWalletUserIdOrderByDateDesc(ID_1L, user.getId()))
                .thenReturn(financialTransactionsList);
        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction1))
                .thenReturn(financialTransactionDTO1);
        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction2))
                .thenReturn(financialTransactionDTO2);

        List<FinancialTransactionDTO> returnedFinancialTransactionDTOsList =
                financialTransactionService.getFinancialTransactionsByWalletId(ID_1L, user.getId());

        //then
        Assertions.assertEquals(returnedFinancialTransactionDTOsList.get(0), financialTransactionDTO1);
        Assertions.assertEquals(returnedFinancialTransactionDTOsList.get(1), financialTransactionDTO2);
    }


    @Test
    @DisplayName("Should not return a transaction if the transaction ID is not found")
    void findFinancialTransaction_InvalidId_NotReturnTransaction() {
        //given
        Long userId = TestUtils.createUserForTest().getId();

        //when
        when(financialTransactionRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(
                        () -> financialTransactionService.findFinancialTransactionForUser(ID_10L, userId))
                .withMessage(ErrorCode.FT001.getBusinessMessage());
    }


    @Test
    @DisplayName("Should return the transaction when provided with a valid transaction ID")
    void findFinancialTransaction_ValidTransactionId_ReturnsTransaction() {
        //given
        User user = TestUtils.createUserForTest();

        FinancialTransaction financialTransaction = createEntityFinancialTransaction(ID_1L);

        FinancialTransactionDTO financialTransactionDTO =
                new FinancialTransactionDTO(ID_1L, BigDecimal.valueOf(20), "description",
                        FinancialTransactionType.EXPENSE, DATE_NOW, null);

        //when
        when(financialTransactionRepository.findByIdAndWalletUserId(ID_1L, user.getId())).thenReturn(Optional.of(financialTransaction));
        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction))
                .thenReturn(financialTransactionDTO);
        FinancialTransactionDTO foundTransaction = financialTransactionService.findFinancialTransactionForUser(ID_1L, user.getId());

        //then
        Assertions.assertEquals(financialTransactionDTO, foundTransaction);
    }

    @Test
    @DisplayName("Should throw an exception when deleting a non-existing financial transaction")
    void deleteTransactionById_InvalidId_ThrowAppRuntimeException() {
        //given
        Long userId = TestUtils.createUserForTest().getId();

        //when
        when(financialTransactionRepository.existsById(ID_1L)).thenReturn(false);

        //then
        Assertions.assertThrows(AppRuntimeException.class, () -> financialTransactionService.deleteTransactionById(ID_1L, userId));
    }

    private FinancialTransaction createEntityFinancialTransaction(Long financialTransactionId) {
        FinancialTransaction ft = new FinancialTransaction();
        ft.setId(financialTransactionId);
        ft.setType(FinancialTransactionType.EXPENSE);
        ft.setDate(DATE_NOW);

        return ft;
    }

    private FinancialTransactionCreateDTO createFinancialTransactionCreateDTO() {
        return new FinancialTransactionCreateDTO(ID_1L, ONE, DESCRIPTION, DATE_NOW, EXPENSE, ID_1L);
    }

    private FinancialTransactionDTO createFinancialTransactionDTO() {
        return new FinancialTransactionDTO(ID_1L, ONE, DESCRIPTION, EXPENSE, DATE_NOW, null);
    }

    private FinancialTransactionUpdateDTO createFinancialTransactionUpdateDTO() {
        return new FinancialTransactionUpdateDTO(TEN, DATE_NOW, DESCRIPTION, EXPENSE, ID_1L);
    }

}
