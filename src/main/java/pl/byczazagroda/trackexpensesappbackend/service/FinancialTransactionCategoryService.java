package pl.byczazagroda.trackexpensesappbackend.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestBody;

import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;


public interface FinancialTransactionCategoryService {

    FinancialTransactionCategoryDTO updateFinancialTransactionCategory(
            @Min(1) @NotNull Long id, @Valid FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO);

    FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO);

    List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories();
}

