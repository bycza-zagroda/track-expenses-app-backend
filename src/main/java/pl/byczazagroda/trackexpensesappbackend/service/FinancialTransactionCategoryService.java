package pl.byczazagroda.trackexpensesappbackend.service;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface FinancialTransactionCategoryService {

    FinancialTransactionCategoryDTO updateFinancialTransactionCategory(
            @Min(1) @NotNull Long id, @Valid FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO);

    FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO);

    List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories();

    void deleteFinancialTransactionCategory(@Min(1) @NotNull Long id);

}

