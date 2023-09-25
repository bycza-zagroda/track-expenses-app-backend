package pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto;

import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record FinancialTransactionCategoryUpdateDTO(@NotBlank String name, @NotNull FinancialTransactionType type) {
}
