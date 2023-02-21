package pl.byczazagroda.trackexpensesappbackend.dto;

import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;

public record FinancialTransactionCreateDTO(@Min(1) @NotNull Long walletId,
                                            @Digits(integer = 13, fraction = 2) @PositiveOrZero BigDecimal amount,
                                            String description, Instant date,
                                            @NotNull FinancialTransactionType type) {
}
