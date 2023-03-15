package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;

import javax.transaction.Transactional;

import javax.validation.Valid;
import java.util.List;

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
    @Transactional
    public FinancialTransactionCategoryDTO updateFinancialTransactionCategory(
            Long id, FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO) {
        FinancialTransactionCategory financialTransactionCategory
                = financialTransactionCategoryRepository.findById(id)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format("Financial transaction category with id: %d not found", id)));
        financialTransactionCategory.setName(financialTransactionCategoryUpdateDTO.name());
        financialTransactionCategory.setType(financialTransactionCategoryUpdateDTO.type());
        FinancialTransactionCategory updatedEntity
                = financialTransactionCategoryRepository.save(financialTransactionCategory);
//        TODO check if category is currently assigned to any financial transaction then throw an error that u cannot update it
        return financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(updatedEntity);
    }

}
