package pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api.dto;

import pl.byczazagroda.trackexpensesappbackend.financialTransaction.api.model.FinancialTransactionType;

public record FinancialTransactionCategoryDTO(Long id, String name, FinancialTransactionType type, Long userId) { }
