package pl.byczazagroda.trackexpensesappbackend.service;


import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;


public interface FinancialTransactionCategoryService {

    FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO);

    List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories();
}

