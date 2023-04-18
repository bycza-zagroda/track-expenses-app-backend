package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType.EXPENSE;

@ExtendWith(MockitoExtension.class)
class FinancialTransactionServiceImplTest{
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

    @Test
    @DisplayName("do not create financial transaction without an existing wallet and throw AppRuntimeException")
    void testCreateFinancialTransaction_WhenWalletNotFound_ThenThrowWalletException(){
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
    void testCreateFinancialTransaction_withValidParameters_returnsFinancialTransactionDTO(){
        //given
        FinancialTransactionCreateDTO financialTransactionCreateDTO = createFinancialTransactionCreateDTO();
        Wallet wallet = new Wallet();
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        FinancialTransaction financialTransaction = createEntityFinancialTransaction();
        when(financialTransactionRepository.save(any())).thenReturn(financialTransaction);
        FinancialTransactionDTO financialTransactionDTO = createFinancialTransactionDTO();
        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(any()))
                .thenReturn(financialTransactionDTO);

        //when
        FinancialTransactionDTO result = financialTransactionService
                .createFinancialTransaction(financialTransactionCreateDTO);

        //then
        assertAll(()->assertEquals(financialTransactionDTO, result), ()->assertEquals(financialTransactionDTO.id(),
                result.id()));
        verify(financialTransactionRepository, atMostOnce()).save(any());
        verify(walletRepository, atMostOnce()).findById(any());
        verify(financialTransactionModelMapper, atMostOnce())
                .mapFinancialTransactionEntityToFinancialTransactionDTO(any());
    }

    @Test
    @DisplayName("create financial transaction with empty description")
    void testCreateFinancialTransaction_WhenDescriptionIsEmpty_ThenCreateFinancialTransaction(){
        //given
        FinancialTransactionCreateDTO financialTransactionCreateDTO = createFinancialTransactionCreateDTO();
        Wallet wallet = new Wallet();
        when(walletRepository.findById(any())).thenReturn(Optional.of(wallet));
        FinancialTransaction financialTransaction = createEntityFinancialTransaction();
        financialTransaction.setDescription(EMPTY);

        when(financialTransactionRepository.save(any())).thenReturn(financialTransaction);
        FinancialTransactionDTO financialTransactionDTO =
                new FinancialTransactionDTO(ID_1L, ONE, EMPTY, EXPENSE, DATE_NOW, null);
        when(financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(any()))
                .thenReturn(financialTransactionDTO);

        //when
        FinancialTransactionDTO result = financialTransactionService
                .createFinancialTransaction(financialTransactionCreateDTO);

        //then
        assertAll(()->assertEquals(EMPTY, financialTransactionDTO.description()),
                ()->assertEquals(financialTransactionDTO, result),
                ()->assertEquals(financialTransactionDTO.id(), result.id()));
        verify(financialTransactionRepository, atMostOnce()).save(any());
        verify(walletRepository, atMostOnce()).findById(any());
        verify(financialTransactionModelMapper, atMostOnce())
                .mapFinancialTransactionEntityToFinancialTransactionDTO(any());
    }

    @Test
    @DisplayName("when finding with proper wallet transaction id should successfully find transactions")
    void shouldSuccessfullyFindFinancialTransactions_WhenWalletIdIsGiven(){
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

        Wallet wallet = new Wallet("Random wallet");
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
    void shouldNotReturnFinancialTransactionById_WhenIdNotExist(){
        //given

        //when
        when(financialTransactionRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(
                ()->financialTransactionService.findById(ID_10L)).withMessage(ErrorCode.FT001.getBusinessMessage());
    }

    @Test
    @DisplayName("when finding with proper financial transaction id should successfully find transaction")
    void shouldSuccessfullyFindFinancialTransaction_WhenFindingWithProperTransactionId(){
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
    void ShouldThrowAnException_WhenGivenTransactionDoesNotExist(){
        //given

        //when
        when(financialTransactionRepository.existsById(ID_1L)).thenReturn(false);

        //then
        Assertions.assertThrows(AppRuntimeException.class, ()->financialTransactionService.deleteTransactionById(ID_1L));
    }

    private FinancialTransaction createEntityFinancialTransaction(){
        FinancialTransaction financialTransaction1 = new FinancialTransaction();
        financialTransaction1.setId(ID_1L);
        financialTransaction1.setType(FinancialTransactionType.EXPENSE);
        financialTransaction1.setDate(DATE_NOW);
        return financialTransaction1;
    }

    private FinancialTransactionCreateDTO createFinancialTransactionCreateDTO(){
        return new FinancialTransactionCreateDTO(ID_1L, ONE, DESCRIPTION, DATE_NOW, EXPENSE);
    }

    private FinancialTransactionDTO createFinancialTransactionDTO(){
        return new FinancialTransactionDTO(ID_1L, ONE, DESCRIPTION, EXPENSE, DATE_NOW, null);
    }
}
