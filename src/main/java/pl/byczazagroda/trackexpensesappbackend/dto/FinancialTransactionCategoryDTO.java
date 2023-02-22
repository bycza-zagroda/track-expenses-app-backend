package pl.byczazagroda.trackexpensesappbackend.dto;

import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;

public record FinancialTransactionCategoryDTO(Long id, String name, FinancialTransactionType type){}
