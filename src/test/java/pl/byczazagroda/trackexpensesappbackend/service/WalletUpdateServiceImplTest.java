package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class}))
class WalletUpdateServiceImplTest {

    public static final long WALLET_ID_1L = 1L;

    public static final long USER_ID_1L = 1L;

    private static final String NAME_1 = "wallet name one";

    private static final String NAME_2 = "wallet name two";

    private static final Instant DATE_NOW = Instant.now();

    private static final Long WALLET_ID = 1L;

    private static final Long USER_ID = 1L;

    private static final String UPDATED_WALLET_NAME = "Updated Wallet Name";

    private static final BigDecimal INCOME_AMOUNT = BigDecimal.valueOf(100);

    private static final BigDecimal EXPENSE_AMOUNT = BigDecimal.valueOf(50);

    private static final String INCOME_DESC = "Income Transaction";

    private static final String EXPENSE_DESC = "Expense Transaction";

    private static final Long CATEGORY_ID_INCOME = 1L;

    private static final Long CATEGORY_ID_EXPENSE = 2L;

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
    private FinancialTransactionModelMapper transactionModelMapper;

    @MockBean
    private FinancialTransactionRepository transactionRepository;

    @Test
    @DisplayName("Should update the wallet when a valid wallet ID is provided")
    void updateWallet_ValidWalletId_UpdatesAndReturnsWalletDTO() {
        // given
        User user = createTestUser();

        WalletUpdateDTO walletUpdateDto = new WalletUpdateDTO(NAME_1);

        Wallet wallet = Wallet.builder()
                .id(WALLET_ID_1L)
                .name(NAME_2)
                .creationDate(DATE_NOW)
                .user(user)
                .build();

        WalletDTO newWalletDTO = new WalletDTO(WALLET_ID_1L, NAME_1, DATE_NOW, USER_ID_1L);
        given(walletRepository.findById(WALLET_ID_1L)).willReturn(Optional.of(wallet));
        given(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).willReturn(newWalletDTO);

        // when
        WalletDTO walletDTO = walletService.updateWallet(WALLET_ID_1L, walletUpdateDto, USER_ID_1L);

        // then
        assertThat(walletDTO.name()).isEqualTo(walletUpdateDto.name());
    }

    @Test
    @DisplayName("Should update the wallet balance correctly when valid transactions are provided")
    void updateWallet_WithValidTransactions_UpdatesBalanceCorrectly() {
        // given
        User user = createTestUser();
        Wallet wallet = createTestWallet(user);
        List<FinancialTransaction> transactions = createTestTransactions();

        mockRepositoryAndMapperBehaviour(wallet, transactions);

        WalletUpdateDTO walletUpdateDTO = new WalletUpdateDTO(UPDATED_WALLET_NAME);

        // when
        WalletDTO result = walletService.updateWallet(WALLET_ID, walletUpdateDTO, USER_ID);

        // then
        Assertions.assertEquals(UPDATED_WALLET_NAME, result.name());
        Assertions.assertEquals(INCOME_AMOUNT.subtract(EXPENSE_AMOUNT), result.balance());
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

    private Wallet createTestWallet(User user) {
        return new Wallet("Test Wallet", user);
    }

    private List<FinancialTransaction> createTestTransactions() {
        FinancialTransaction transaction1 = createFinancialTransaction(INCOME_AMOUNT,
                FinancialTransactionType.INCOME, INCOME_DESC);
        FinancialTransaction transaction2 = createFinancialTransaction(EXPENSE_AMOUNT,
                FinancialTransactionType.EXPENSE, EXPENSE_DESC);
        return Arrays.asList(transaction1, transaction2);
    }

    private FinancialTransaction createFinancialTransaction(BigDecimal amount,
                                                            FinancialTransactionType type, String description) {
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDate(Instant.now());
        transaction.setDescription(description);
        return transaction;
    }

    private void mockRepositoryAndMapperBehaviour(Wallet wallet, List<FinancialTransaction> transactions) {
        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findAllByWalletIdAndWalletUserIdOrderByDateDesc
                (WALLET_ID, USER_ID)).thenReturn(transactions);

        for (FinancialTransaction transaction : transactions) {
            FinancialTransactionDTO dto = new FinancialTransactionDTO(
                    transaction.getId(),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getType(),
                    transaction.getDate(),
                    transaction.getFinancialTransactionCategory() !=
                            null ? transaction.getFinancialTransactionCategory().getId() : null
            );

            when(transactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO
                    (eq(transaction)))
                    .thenReturn(dto);
        }
    }

}
