package pl.byczazagroda.trackexpensesappbackend.service;


import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Validated
public interface FinancialTransactionCategoryService {

    FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO);

    void deleteFinancialTransactionCategory(@Min(1) @NotNull Long id);

    List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories();

    FinancialTransactionCategoryDTO updateFinancialTransactionCategory(
            @Min(1) @NotNull Long id,
            @Valid FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO);

}

