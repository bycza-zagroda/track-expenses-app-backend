package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;

@Service
@RequiredArgsConstructor
@Validated
public class FinancialTransactionCategoryServiceImpl implements FinancialTransactionCategoryService {

    private final FinancialTransactionCategoryRepository financialTransactionCategoryRepository;
    private final FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;

    @Override
    public FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO) {
        FinancialTransactionCategory financialTransactionCategory = new FinancialTransactionCategory(
                financialTransactionCategoryCreateDTO.name(), financialTransactionCategoryCreateDTO.type());
        FinancialTransactionCategory savedFinancialTransaction =
                financialTransactionCategoryRepository.save(financialTransactionCategory);
        return financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(savedFinancialTransaction);
    }
}