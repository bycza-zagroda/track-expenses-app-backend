package pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto;

import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;

public record FinancialTransactionCategoryDTO(Long id, String name, FinancialTransactionType type, Long userId) { }
