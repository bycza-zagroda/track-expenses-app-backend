package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


@RequiredArgsConstructor
@Service
@Validated
public class FinancialTransactionCategoryServiceImpl implements FinancialTransactionCategoryService {

    private final FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;
    
    private final FinancialTransactionCategoryRepository financialTransactionCategoryRepository;


    @Override
    public FinancialTransactionCategoryDTO createFinancialTransactionCategory(@Valid
            FinancialTransactionCategoryCreateDTO dto) {

        FinancialTransactionCategory entityToSave = new FinancialTransactionCategory(dto.name(), dto.type());
        FinancialTransactionCategory savedEntity = financialTransactionCategoryRepository.save(entityToSave);

        return financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(savedEntity);
    }
      
    @Override
    public List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories() {
        return financialTransactionCategoryRepository.findAll().stream()
                .map(financialTransactionCategoryModelMapper::mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO)
                .toList();
    }

    @Override
    public void deleteFinancialTransactionCategory(@Min(1) @NotNull Long id) {
        if(financialTransactionCategoryRepository.existsById(id)) {
            financialTransactionCategoryRepository.deleteById(id);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.FTC001,
                    String.format("Financial transaction category with given id: %d does not exist", id));
        }
    }

}