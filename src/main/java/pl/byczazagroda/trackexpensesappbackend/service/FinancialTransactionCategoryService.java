package pl.byczazagroda.trackexpensesappbackend.service;

import org.springframework.web.bind.annotation.RequestBody;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;

import javax.validation.Valid;

public interface FinancialTransactionCategoryService {

    FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO);
}