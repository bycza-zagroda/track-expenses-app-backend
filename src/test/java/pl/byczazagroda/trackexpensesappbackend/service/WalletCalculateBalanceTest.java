package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.impl.WalletServiceImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Stream.of;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class WalletCalculateBalanceTest {

    @Mock
    private FinancialTransactionModelMapper modelMapper;

    @Mock
    private FinancialTransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletModelMapper walletModelMapper;

    @Mock
    private AuthRepository authRepository;

    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        walletService = new WalletServiceImpl(walletRepository, walletModelMapper,
                authRepository, transactionRepository, modelMapper );
    }

    @ParameterizedTest
    @MethodSource("provideTransactionScenarios")
    @DisplayName("Calculate balance for various transaction scenarios")
    void calculateBalance_MultipleScenarios_CorrectBalance(
            List<FinancialTransactionDTO> transactions, BigDecimal expectedBalance) {
        assertEquals(expectedBalance, walletService.calculateCurrentBalance(transactions));
    }

    private static Stream<Arguments> provideTransactionScenarios() {
        return of(
                arguments(
                        asList(
                                createTestTransaction(1L, "100.00",
                                        FinancialTransactionType.INCOME, 1L),
                                createTestTransaction(2L, "50.00",
                                        FinancialTransactionType.EXPENSE, 2L),
                                createTestTransaction(3L, "200.00",
                                        FinancialTransactionType.INCOME, 3L),
                                createTestTransaction(4L, "75.00",
                                        FinancialTransactionType.EXPENSE, 4L)
                        ),
                        new BigDecimal("175.00") // 100 + 200 - 50 - 75
                ),
                arguments(
                        asList(
                                createTestTransaction(5L, "0.00",
                                        FinancialTransactionType.INCOME, 5L),
                                createTestTransaction(6L, "1000000.00",
                                        FinancialTransactionType.EXPENSE, 6L)
                        ),
                        new BigDecimal("-1000000.00") // 0 - 1000000
                ),
                arguments(emptyList(), new BigDecimal("0")),
                arguments(
                        asList(
                                createTestTransaction(7L, "10.00",
                                        FinancialTransactionType.INCOME, 7L),
                                createTestTransaction(8L, "5.00",
                                        FinancialTransactionType.EXPENSE, 8L),
                                createTestTransaction(9L, "20.00",
                                        FinancialTransactionType.INCOME, 9L),
                                createTestTransaction(10L, "10.00",
                                        FinancialTransactionType.EXPENSE, 10L)
                        ),
                        new BigDecimal("15.00") // 10 + 20 - 5 - 10
                ),
                arguments(
                        asList(
                                createTestTransaction(11L, "100.00",
                                        FinancialTransactionType.INCOME, 11L),
                                createTestTransaction(12L, "100.00",
                                        FinancialTransactionType.EXPENSE, 12L)
                        ),
                        new BigDecimal("0.00") // 100 - 100
                )
        );
    }

    private static FinancialTransactionDTO createTestTransaction(
            Long id, String amount, FinancialTransactionType type, Long categoryId) {
        return new FinancialTransactionDTO(id, new BigDecimal(
                amount), "Test Transaction", type, Instant.now(), categoryId);
    }

}
