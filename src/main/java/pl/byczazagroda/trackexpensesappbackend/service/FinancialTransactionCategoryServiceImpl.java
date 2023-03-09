package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;

import javax.validation.Valid;

@RequiredArgsConstructor
@Service
@Validated
public class FinancialTransactionCategoryServiceImpl implements FinancialTransactionCategoryService {

    private final FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    private final FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;

    @Override
    public FinancialTransactionCategoryDTO createFinancialTransactionCategory(@Valid
            FinancialTransactionCategoryCreateDTO dto) {

        FinancialTransactionCategory entityToSave = new FinancialTransactionCategory(dto.name(), dto.type());
        FinancialTransactionCategory savedEntity = financialTransactionCategoryRepository.save(entityToSave);

        return financialTransactionCategoryModelMapper.
                mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(savedEntity);
    }
}

