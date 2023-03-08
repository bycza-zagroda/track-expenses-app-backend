package pl.byczazagroda.trackexpensesappbackend.dto;

import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record FinancialTransactionCategoryCreateDTO(@NotBlank @Size(max = 30, message = "CATEGORY NAME TOO LONG") String name,
                                                    @NotNull FinancialTransactionType type){}
