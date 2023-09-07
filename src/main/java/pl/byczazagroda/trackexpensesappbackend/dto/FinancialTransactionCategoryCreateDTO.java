package pl.byczazagroda.trackexpensesappbackend.dto;

import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.regex.RegexConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public record FinancialTransactionCategoryCreateDTO(
        @NotBlank
        @Pattern(regexp = RegexConstant.CATEGORY_NAME_PATTERN)
        String name,
        @NotNull FinancialTransactionType type) {
}
