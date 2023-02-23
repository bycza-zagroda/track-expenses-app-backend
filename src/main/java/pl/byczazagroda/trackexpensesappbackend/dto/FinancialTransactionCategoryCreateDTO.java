package pl.byczazagroda.trackexpensesappbackend.dto;

import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record FinancialTransactionCategoryCreateDTO(@NotBlank String name, @NotNull FinancialTransactionType type){}
