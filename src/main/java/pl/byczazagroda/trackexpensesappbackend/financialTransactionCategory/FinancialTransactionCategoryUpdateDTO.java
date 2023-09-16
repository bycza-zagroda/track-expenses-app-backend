package pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory;

import pl.byczazagroda.trackexpensesappbackend.financialTransaction.FinancialTransactionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record FinancialTransactionCategoryUpdateDTO(@NotBlank String name, @NotNull FinancialTransactionType type) {
}
