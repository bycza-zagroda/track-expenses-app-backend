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
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType.EXPENSE;
import static pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType.INCOME;

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
    @DisplayName("When financial transaction type and financial transaction category type are different throw AppRuntimeException")
    void testCreateFinancialTransaction_WhenTransactionTypeDoesNotMatchCategoryType_ThenThrowAppRunTimeException() {
        //given
        FinancialTransactionCreateDTO ftCreateDTO = createFinancialTransactionCreateDTO();
        Wallet wallet = new Wallet();
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        FinancialTransaction ft = createEntityFinancialTransaction();
        when(financialTransactionRepository.save(any())).thenReturn(ft);
        FinancialTransactionDTO ftDTO = createFinancialTransactionDTO();
        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(any()))
                .thenReturn(ftDTO);
        FinancialTransactionCategory ftCategory = createFinancialTransactionCategory(INCOME);
        when(financialTransactionCategoryRepository.findById(any())).thenReturn(Optional.of(ftCategory));

        //when & then
        AppRuntimeException exception = assertThrows(
                AppRuntimeException.class,
                () -> financialTransactionService.createFinancialTransaction(ftCreateDTO));
        assertEquals(ErrorCode.FT002.getBusinessStatusCode(), exception.getBusinessStatusCode());
    }

    @NotNull
    private FinancialTransactionCategory createFinancialTransactionCategory(FinancialTransactionType type) {
        return new FinancialTransactionCategory(ID_1L, "Category name", type, DATE_NOW, null, new User());
    }

    @Test
    @DisplayName("do not create financial transaction without an existing wallet and throw AppRuntimeException")
    void testCreateFinancialTransaction_WhenWalletNotFound_ThenThrowWalletException() {
        //given
        FinancialTransactionCreateDTO financialTransactionCreateDTO = createFinancialTransactionCreateDTO();
        when(walletRepository.findById(any())).thenReturn(Optional.empty());

        //when & then
        AppRuntimeException exception = assertThrows(
                AppRuntimeException.class,
                () -> financialTransactionService.createFinancialTransaction(financialTransactionCreateDTO)
        );
        assertEquals(ErrorCode.W003.getBusinessStatusCode(), exception.getBusinessStatusCode());
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("create financial transaction when valid parameters are given")
    void testCreateFinancialTransaction_withValidParameters_returnsFinancialTransactionDTO() {
        //given
        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                ID_1L, ONE, EMPTY, DATE_NOW, EXPENSE, ID_1L);
        Wallet wallet = new Wallet();
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        FinancialTransaction financialTransaction = createEntityFinancialTransaction();
        when(financialTransactionRepository.save(any())).thenReturn(financialTransaction);
        FinancialTransactionDTO financialTransactionDTO = createFinancialTransactionDTO();
        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(any()))
                .thenReturn(financialTransactionDTO);
        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(EXPENSE);
        when(financialTransactionCategoryRepository.findById(any())).thenReturn(Optional.of(financialTransactionCategory));

        //when
        FinancialTransactionDTO result = financialTransactionService
                .createFinancialTransaction(financialTransactionCreateDTO);

        //then
        assertAll(() -> assertEquals(financialTransactionDTO, result), () -> assertEquals(financialTransactionDTO.id(),
                result.id()));
        verify(financialTransactionRepository, atMostOnce()).save(any());
        verify(walletRepository, atMostOnce()).findById(any());
        verify(financialTransactionModelMapper, atMostOnce())
                .mapFinancialTransactionEntityToFinancialTransactionDTO(any());
    }

    @Test
    @DisplayName("create financial transaction with empty description")
    void testCreateFinancialTransaction_WhenDescriptionIsEmpty_ThenCreateFinancialTransaction() {
        //given
        FinancialTransactionCreateDTO financialTransactionCreateDTO = new FinancialTransactionCreateDTO(
                ID_1L, ONE, EMPTY, DATE_NOW, EXPENSE, ID_1L);
        Wallet wallet = new Wallet();
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        FinancialTransaction financialTransaction = createEntityFinancialTransaction();
        financialTransaction.setDescription(EMPTY);
        financialTransaction.setAmount(ONE);
        when(financialTransactionRepository.save(any())).thenReturn(financialTransaction);
        FinancialTransactionDTO financialTransactionDTO =
                new FinancialTransactionDTO(ID_1L, ONE, EMPTY, EXPENSE, DATE_NOW, ID_1L);
        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(any()))
                .thenReturn(financialTransactionDTO);
        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(EXPENSE);
        when(financialTransactionCategoryRepository.findById(any())).thenReturn(Optional.of(financialTransactionCategory));

        //when
        FinancialTransactionDTO result = financialTransactionService
                .createFinancialTransaction(financialTransactionCreateDTO);

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
    @DisplayName("do not update financial transaction without valid id and throw AppRuntimeException")
    void shouldThrowExceptionWhenUpdatingFinancialTransactionWithInvalidId() {
        //given
        FinancialTransactionUpdateDTO updateDTO = createFinancialTransactionUpdateDTO();
        when(financialTransactionRepository.findById(any())).thenReturn(Optional.empty());

        //when & then
        AppRuntimeException exception = assertThrows(
                AppRuntimeException.class,
                () -> financialTransactionService.updateFinancialTransaction(ID_1L, updateDTO)
        );
        assertEquals(ErrorCode.FT001.getBusinessStatusCode(), exception.getBusinessStatusCode());
        assertEquals(ErrorCode.FT001.getBusinessMessage(), exception.getBusinessMessage());
        verify(financialTransactionRepository, atMostOnce()).findById(any());
        verify(financialTransactionModelMapper, never()).mapFinancialTransactionEntityToFinancialTransactionDTO(any());
    }

    @Test
    @DisplayName("update financial transaction with valid parameters")
    void shouldUpdateFinancialTransactionWhenValidParametersAreGiven() {
        //given
        FinancialTransactionUpdateDTO updateDTO = createFinancialTransactionUpdateDTO();

        FinancialTransaction financialTransaction = createEntityFinancialTransaction();
        financialTransaction.setDescription(EMPTY);
        financialTransaction.setAmount(ONE);

        FinancialTransactionDTO financialTransactionDTO
                = new FinancialTransactionDTO(ID_1L, TEN, DESCRIPTION, EXPENSE, DATE_NOW, null);

        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction))
                .thenReturn(financialTransactionDTO);

        when(financialTransactionRepository
                .findById(ID_1L))
                .thenReturn(Optional.of(financialTransaction));

        //when
        FinancialTransactionDTO result = financialTransactionService.updateFinancialTransaction(ID_1L, updateDTO);

        //then
        assertAll(
                () -> assertEquals(updateDTO.amount(), result.amount()),
                () -> assertEquals(updateDTO.description(), result.description()),
                () -> assertEquals(updateDTO.type(), result.type()),
                () -> assertEquals(updateDTO.date(), result.date()));

        verify(financialTransactionRepository, atMostOnce()).save(any());
        verify(financialTransactionRepository, atMostOnce()).findById(any());
        verify(financialTransactionModelMapper, atMostOnce())
                .mapFinancialTransactionEntityToFinancialTransactionDTO(any());
    }

    @Test
    @DisplayName("when finding with proper wallet transaction id should successfully find transactions")
    void shouldSuccessfullyFindFinancialTransactions_WhenWalletIdIsGiven() {
        //given
        FinancialTransaction financialTransaction1 = createEntityFinancialTransaction();
        FinancialTransactionDTO financialTransactionDTO1 = createFinancialTransactionDTO();

        FinancialTransaction financialTransaction2 = createEntityFinancialTransaction();
        financialTransaction2.setId(ID_2L);

        FinancialTransactionDTO financialTransactionDTO2 =
                new FinancialTransactionDTO(ID_2L, BigDecimal.ONE, "desc",
                        FinancialTransactionType.EXPENSE, DATE_NOW, null);

        List<FinancialTransaction> financialTransactionsList = new ArrayList<>();
        financialTransactionsList.add(financialTransaction1);
        financialTransactionsList.add(financialTransaction2);

        Wallet wallet = new Wallet("Random wallet", createTestUser());
        wallet.setId(ID_1L);
        wallet.setCreationDate(DATE_NOW);

        financialTransaction1.setWallet(wallet);
        financialTransaction2.setWallet(wallet);

        wallet.setFinancialTransactionList(financialTransactionsList);

        //when
        when(walletRepository.existsById(ID_1L)).thenReturn(true);
        when(financialTransactionRepository.findAllByWalletIdOrderByDateDesc(ID_1L))
                .thenReturn(financialTransactionsList);
        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction1))
                .thenReturn(financialTransactionDTO1);
        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction2))
                .thenReturn(financialTransactionDTO2);

        List<FinancialTransactionDTO> returnedFinancialTransactionDTOsList =
                financialTransactionService.getFinancialTransactionsByWalletId(ID_1L);

        //then
        Assertions.assertEquals(returnedFinancialTransactionDTOsList.get(0), financialTransactionDTO1);
        Assertions.assertEquals(returnedFinancialTransactionDTOsList.get(1), financialTransactionDTO2);
    }


    @Test
    @DisplayName("when financial transaction id doesn't exist should not return transaction")
    void shouldNotReturnFinancialTransactionById_WhenIdNotExist() {
        //given

        //when
        when(financialTransactionRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(
                () -> financialTransactionService.findById(ID_10L)).withMessage(ErrorCode.FT001.getBusinessMessage());
    }

    @Test
    @DisplayName("when finding with proper financial transaction id should successfully find transaction")
    void shouldSuccessfullyFindFinancialTransaction_WhenFindingWithProperTransactionId() {
        //given
        FinancialTransaction financialTransaction = createEntityFinancialTransaction();
        FinancialTransactionDTO financialTransactionDTO =
                new FinancialTransactionDTO(ID_1L, BigDecimal.valueOf(20), "description",
                        FinancialTransactionType.EXPENSE, DATE_NOW, null);

        //when
        when(financialTransactionRepository.findById(ID_1L)).thenReturn(Optional.of(financialTransaction));
        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction))
                .thenReturn(financialTransactionDTO);
        FinancialTransactionDTO foundTransaction = financialTransactionService.findById(ID_1L);

        //then
        Assertions.assertEquals(financialTransactionDTO, foundTransaction);
    }

    @Test
    @DisplayName("when deleting financial transaction that does not exist should throw an exception")
    void ShouldThrowAnException_WhenGivenTransactionDoesNotExist() {
        //given

        //when
        when(financialTransactionRepository.existsById(ID_1L)).thenReturn(false);

        //then
        Assertions.assertThrows(AppRuntimeException.class, () -> financialTransactionService.deleteTransactionById(ID_1L));
    }

    private FinancialTransaction createEntityFinancialTransaction() {
        FinancialTransaction financialTransaction1 = new FinancialTransaction();
        financialTransaction1.setId(ID_1L);
        financialTransaction1.setType(FinancialTransactionType.EXPENSE);
        financialTransaction1.setDate(DATE_NOW);
        return financialTransaction1;
    }

    private FinancialTransactionCreateDTO createFinancialTransactionCreateDTO() {
        return new FinancialTransactionCreateDTO(ID_1L, ONE, DESCRIPTION, DATE_NOW, EXPENSE, ID_1L);
    }

    private FinancialTransactionDTO createFinancialTransactionDTO() {
        return new FinancialTransactionDTO(ID_1L, ONE, DESCRIPTION, EXPENSE, DATE_NOW, null);
    }

    private FinancialTransactionUpdateDTO createFinancialTransactionUpdateDTO() {
        return new FinancialTransactionUpdateDTO(TEN, DATE_NOW, DESCRIPTION, EXPENSE, null);
    }

    private User createTestUser() {
        return User.builder()
                .id(1L)
                .userName("userone")
                .email("Email@wp.pl")
                .password("password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
    }
}
