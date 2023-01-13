package pl.byczazagroda.trackexpensesappbackend.dto;

import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateFinancialTransactionDTO(@Min(1) @NotNull Long walletId, @DecimalMin("0.0") BigDecimal amount,
                                            String description, FinancialTransactionType financialTransactionType) {
}
