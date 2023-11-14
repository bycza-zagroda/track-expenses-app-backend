package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class WalletUpdateBalanceTest {

    @ParameterizedTest
    @MethodSource("provideTransactionScenarios")
    @DisplayName("Calculate balance for various transaction scenarios")
    void calculateBalance_MultipleScenarios_CorrectBalance(List<FinancialTransaction> transactions,
                                                           BigDecimal expectedBalance) {
        Wallet wallet = new Wallet();
        wallet.setFinancialTransactionList(transactions);
        assertEquals(expectedBalance, wallet.calculateCurrentBalance());
    }

    private static Stream<Arguments> provideTransactionScenarios() {
        return Stream.of(
                // Typical scenario: mix of INCOME and EXPENSE transactions
                Arguments.of(
                        Arrays.asList(
                                createTestTransaction(new BigDecimal("100.00"), FinancialTransactionType.INCOME, Instant.now()),
                                createTestTransaction(new BigDecimal("50.00"), FinancialTransactionType.EXPENSE, Instant.now()),
                                createTestTransaction(new BigDecimal("200.00"), FinancialTransactionType.INCOME, Instant.now()),
                                createTestTransaction(new BigDecimal("75.00"), FinancialTransactionType.EXPENSE, Instant.now())
                        ),
                        new BigDecimal("175.00") // 100 + 200 - 50 - 75
                ),
                // Extreme amounts
                Arguments.of(
                        Arrays.asList(
                                createTestTransaction(new BigDecimal("0.00"), FinancialTransactionType.INCOME, Instant.now()),
                                createTestTransaction(new BigDecimal("1000000.00"), FinancialTransactionType.EXPENSE, Instant.now())
                        ),
                        new BigDecimal("-1000000.00") // 0 - 1000000
                ),
                // Empty wallet with no transactions
                Arguments.of(
                        Arrays.asList(),
                        new BigDecimal("0")
                ),
                // Multiple small transactions
                Arguments.of(
                        Arrays.asList(
                                createTestTransaction(new BigDecimal("10.00"), FinancialTransactionType.INCOME, Instant.now()),
                                createTestTransaction(new BigDecimal("5.00"), FinancialTransactionType.EXPENSE, Instant.now()),
                                createTestTransaction(new BigDecimal("20.00"), FinancialTransactionType.INCOME, Instant.now()),
                                createTestTransaction(new BigDecimal("10.00"), FinancialTransactionType.EXPENSE, Instant.now())
                        ),
                        new BigDecimal("15.00") // 10 + 20 - 5 - 10
                ),
                // Transactions with the same value
                Arguments.of(
                        Arrays.asList(
                                createTestTransaction(new BigDecimal("100.00"), FinancialTransactionType.INCOME, Instant.now()),
                                createTestTransaction(new BigDecimal("100.00"), FinancialTransactionType.EXPENSE, Instant.now())
                        ),
                        new BigDecimal("0.00") // 100 - 100
                ),
                // Transactions from different time periods
                Arguments.of(
                        Arrays.asList(
                                createTestTransaction(new BigDecimal("100.00"), FinancialTransactionType.INCOME, Instant.now().minusSeconds(3600)),
                                createTestTransaction(new BigDecimal("50.00"), FinancialTransactionType.EXPENSE, Instant.now().plusSeconds(3600))
                        ),
                        new BigDecimal("50.00") // 100 - 50
                )

        );
    }

    private static FinancialTransaction createTestTransaction(BigDecimal amount, FinancialTransactionType type, Instant date) {
        return FinancialTransaction.builder()
                .amount(amount)
                .type(type)
                .date(date)
                .description("Test Transaction")
                .build();
    }

}
