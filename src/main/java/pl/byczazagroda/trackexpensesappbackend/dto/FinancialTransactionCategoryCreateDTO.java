package pl.byczazagroda.trackexpensesappbackend.dto;

import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record FinancialTransactionCategoryCreateDTO(@Min(1) @NotNull Long id,
                                                    @NotBlank String name,
                                                    @NotNull FinancialTransactionType type){}
