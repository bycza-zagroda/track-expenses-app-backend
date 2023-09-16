package pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api.dto;

import pl.byczazagroda.trackexpensesappbackend.financialTransaction.api.model.FinancialTransactionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record FinancialTransactionCategoryUpdateDTO(@NotBlank String name, @NotNull FinancialTransactionType type) {
}
