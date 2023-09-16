package pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory;

import pl.byczazagroda.trackexpensesappbackend.financialTransaction.FinancialTransactionType;

public record FinancialTransactionCategoryDTO(Long id, String name, FinancialTransactionType type, Long userId) { }
