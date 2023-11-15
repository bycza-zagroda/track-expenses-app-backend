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
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.wallet.WalletController;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.general.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.impl.WalletServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class}))
class WalletGetServiceImplTest {

    public static final long WALLET_ID_1L = 1L;

    public static final long USER_ID_1L = 1L;

    public static final long ID_5L = 5L;

    private static final String NAME_1 = "wallet name one";

    private static final Instant DATE_NOW = Instant.now();

    @MockBean
    private ErrorStrategy errorStrategy;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private AuthRepository userRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @MockBean
    private WalletModelMapper walletModelMapper;

    @MockBean
    private FinancialTransactionModelMapper financialTransactionModelMapper;

    @MockBean
    private FinancialTransactionRepository financialTransactionRepository;

    @Test
    @DisplayName("Should not return a wallet if the wallet ID is not found during update")
    void updateWallet_WalletIdNotFound_ThrowAppRuntimeException() {
        // given
        Long userId = 1L;
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        WalletUpdateDTO walletUpdateDto = new WalletUpdateDTO(NAME_1);

        // when

        // then
        assertThatThrownBy(() -> walletService.updateWallet(WALLET_ID_1L, walletUpdateDto, userId)).isInstanceOf(AppRuntimeException.class);
    }

    @Test
    @DisplayName("Should find and return a wallet with the proper ID")
    void findById_ValidWalletId_ReturnsWalletDTO() {
        //given
        Wallet wallet = Wallet.builder()
                .id(WALLET_ID_1L)
                .name(NAME_1)
                .creationDate(DATE_NOW)
                .user(createTestUser())
                .build();

        WalletDTO expectedDTO = new WalletDTO(WALLET_ID_1L, NAME_1, DATE_NOW, USER_ID_1L);

        //when
        when(walletRepository.existsById(WALLET_ID_1L)).thenReturn(true);
        when(walletRepository.findById(WALLET_ID_1L)).thenReturn(Optional.of(wallet));
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(expectedDTO);

        WalletDTO foundWallet = walletService.findById(WALLET_ID_1L, USER_ID_1L);

        //then
        Assertions.assertEquals(expectedDTO, foundWallet);
    }

    @Test
    @DisplayName("Should not return a wallet if the wallet ID is not found")
    void findById_WalletIdNotFound_ThrowAppRuntimeException() {
        //given

        //when
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> walletService.findById(ID_5L, USER_ID_1L)).isInstanceOf(AppRuntimeException.class);
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(() ->
                walletService.findById(ID_5L, USER_ID_1L)).withMessage(ErrorCode.W003.getBusinessMessage());
    }

    @Test
    @DisplayName("Should return all wallets containing the specified name pattern")
    void findAllByNameIgnoreCase_NamePatternProvided_ReturnsFilteredWallets() {
        // given
        User user = createTestUser();
        String walletNameSearched = "Family";
        List<Wallet> walletList = createListOfWalletsByName(user,
                "Family wallet", "Common Wallet", "Smith Family Wallet", "Random family wallet");
        List<WalletDTO> walletListDTO = walletList.stream().map((Wallet x) ->
                new WalletDTO(x.getId(), x.getName(), x.getCreationDate(), 1L)).toList();
        given(walletRepository.findAll()).willReturn(walletList);


        given(walletRepository.findAllByUserIdAndNameIsContainingIgnoreCase(USER_ID_1L, walletNameSearched))
                .willReturn(walletList.stream()
                                .filter(wallet -> wallet.getName().contains(walletNameSearched))
                                .toList());



        walletList.forEach(wallet -> given(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).willReturn(
                walletListDTO
                        .stream()
                        .filter(walletDTO -> wallet.getName().toUpperCase().contains(walletNameSearched.toUpperCase()))
                        .findAny()
                        .orElse(null)));
        // when
        List<WalletDTO> fundedWallets = walletService.findAllByNameIgnoreCase(walletNameSearched, USER_ID_1L);

        // then
        assertThat(fundedWallets, hasSize(walletRepository.findAllByUserIdAndNameIsContainingIgnoreCase(USER_ID_1L, walletNameSearched).size()));
    }

    @Test
    @DisplayName("Should correctly calculate the balance of wallets when retrieving them")
    void getWallets_CalculatesBalancesCorrectly() {
        // given
        Long userId = 1L;
        User user = new User();
        Wallet wallet1 = new Wallet("Wallet 1", user);
        wallet1.setId(1L);
        Wallet wallet2 = new Wallet("Wallet 2", user);
        wallet2.setId(2L);

        List<FinancialTransaction> transactionsForWallet1 = createTestTransactionsForWallet(wallet1);
        List<FinancialTransaction> transactionsForWallet2 = createTestTransactionsForWallet(wallet2);

        mockTransactionMapping(transactionsForWallet1, financialTransactionModelMapper);
        mockTransactionMapping(transactionsForWallet2, financialTransactionModelMapper);

        when(walletRepository.findAllByUserIdOrderByNameAsc(userId))
                .thenReturn(Arrays.asList(wallet1, wallet2));
        when(financialTransactionRepository.findAllByWalletIdOrderByDateDesc(
                wallet1.getId())).thenReturn(transactionsForWallet1);
        when(financialTransactionRepository.findAllByWalletIdOrderByDateDesc(
                wallet2.getId())).thenReturn(transactionsForWallet2);

        // when
        List<WalletDTO> result = walletService.getWallets(userId);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(new BigDecimal("50.00"), result.get(0).balance()); // for wallet 1
        Assertions.assertEquals(new BigDecimal("200.00"), result.get(1).balance()); // for wallet 2
    }

    private List<FinancialTransaction> createTestTransactionsForWallet(Wallet wallet) {
        FinancialTransaction incomeTransaction1 = FinancialTransaction.builder()
                .id(1L)
                .wallet(wallet)
                .type(FinancialTransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .date(Instant.now())
                .description("Test Income 1")
                .build();

        FinancialTransaction incomeTransaction2 = FinancialTransaction.builder()
                .id(3L)
                .wallet(wallet)
                .type(FinancialTransactionType.INCOME)
                .amount(new BigDecimal("100.00"))
                .date(Instant.now())
                .description("Test Income 2")
                .build();

        FinancialTransaction expenseTransaction = FinancialTransaction.builder()
                .id(2L)
                .wallet(wallet)
                .type(FinancialTransactionType.EXPENSE)
                .amount(new BigDecimal("50.00"))
                .date(Instant.now())
                .description("Test Expense")
                .build();

        return wallet.getId() == 1L
                ? Arrays.asList(incomeTransaction1, expenseTransaction) // Wallet 1: 100 - 50 = 50
                : Arrays.asList(incomeTransaction1, incomeTransaction2); // Wallet 2: 100 + 100 = 200
    }

    private void mockTransactionMapping(List<FinancialTransaction> transactions,
                                        FinancialTransactionModelMapper mapper) {
        transactions.forEach(transaction -> {
            Long categoryId = transaction.getFinancialTransactionCategory() != null ? transaction
                    .getFinancialTransactionCategory().getId() : null;
            FinancialTransactionDTO dto = new FinancialTransactionDTO(
                    transaction.getId(),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getType(),
                    transaction.getDate(),
                    categoryId
            );
            when(mapper
                    .mapFinancialTransactionEntityToFinancialTransactionDTO(transaction)).thenReturn(dto);
        });
    }

    private List<Wallet> createListOfWalletsByName(User user, String... names) {
        return Arrays.stream(names).map(name -> new Wallet(name, user)).toList();
    }

    private User createTestUser() {
        return User.builder()
                .id(USER_ID_1L)
                .userName("userone")
                .email("Email@wp.pl")
                .password("password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
    }
}
