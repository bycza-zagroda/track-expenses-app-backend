package pl.byczazagroda.trackexpensesappbackend.service;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestBody;

import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;


public interface FinancialTransactionCategoryService {

    FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO);

    List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories();

    void deleteFinancialTransactionCategory(@Min(1) @NotNull Long id);

}

