package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;

import java.util.List;

public interface FinancialTransactionCategoryService {
    List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories();
}
