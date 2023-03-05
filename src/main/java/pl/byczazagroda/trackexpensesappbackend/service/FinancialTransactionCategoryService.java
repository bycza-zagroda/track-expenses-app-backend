package pl.byczazagroda.trackexpensesappbackend.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDetailedDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface FinancialTransactionCategoryService {

    FinancialTransactionCategoryDetailedDTO getFinancialTransactionCategoryById(@Min(1) @NotNull @PathVariable Long id);

    List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories();

    FinancialTransactionCategoryDTO createFinancialTransactionCategory(@Valid @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO);

    FinancialTransactionCategoryDTO updateFinancialTransactionCategory(@Min(1) @NotNull @PathVariable Long id, @Valid @RequestBody FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO);

    void deleteFinancialTransactionCategoryById(@Min(1) @NotNull @PathVariable Long id);
}