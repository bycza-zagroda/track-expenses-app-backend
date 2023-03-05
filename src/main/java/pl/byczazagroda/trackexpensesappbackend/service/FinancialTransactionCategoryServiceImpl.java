package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDetailedDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class FinancialTransactionCategoryServiceImpl implements FinancialTransactionCategoryService {

    private final FinancialTransactionCategoryRepository financialTransactionCategoryRepository;
    private final FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;
    private final FinancialTransactionRepository financialTransactionRepository;

    @Override
    public FinancialTransactionCategoryDTO createFinancialTransactionCategory(FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO) {
        FinancialTransactionCategory financialTransactionCategory = new FinancialTransactionCategory(
                financialTransactionCategoryCreateDTO.name(), financialTransactionCategoryCreateDTO.type());
        FinancialTransactionCategory savedFinancialTransaction = financialTransactionCategoryRepository.save(financialTransactionCategory);
        return financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(savedFinancialTransaction);
    }

    @Override
    public FinancialTransactionCategoryDetailedDTO getFinancialTransactionCategoryById(@Min(1) @NotNull @PathVariable Long id) {
        FinancialTransactionCategory financialTransactionCategory = financialTransactionCategoryRepository.findById(id)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001, String.format("Financial transaction category with id: %d not found", id)));
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory);
        BigInteger financialTransactionCounter = BigInteger.valueOf(financialTransactionRepository.countByType(financialTransactionCategory.getType()));
        return new FinancialTransactionCategoryDetailedDTO(financialTransactionCategoryDTO, financialTransactionCounter);
    }

    @Override
    public List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories() {
        return financialTransactionCategoryRepository.findAll().stream()
                .map(financialTransactionCategoryModelMapper::mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO)
                .toList();
    }

    @Override
    public void deleteFinancialTransactionCategoryById(@Min(1) @NotNull @PathVariable Long id) {
        if (financialTransactionCategoryRepository.existsById(id)) {
            financialTransactionCategoryRepository.deleteById(id);
        } else {
            throw new AppRuntimeException(ErrorCode.FTC001,
                    String.format("Financial Transaction Category with given id: %d not found", id));
        }
    }

    @Override
    @Transactional
    public FinancialTransactionCategoryDTO updateFinancialTransactionCategory(@Min(1) @NotNull @PathVariable Long id,
                                                                              @Valid @RequestBody FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO) {
        FinancialTransactionCategory entityToUpdate = financialTransactionCategoryRepository.findById(id)
                .orElseThrow(() -> {
                    throw new AppRuntimeException(ErrorCode.FTC001,
                            String.format("Transaction category with id: %d not found", id));
                });
        entityToUpdate.setName(financialTransactionCategoryUpdateDTO.name());
        entityToUpdate.setType(financialTransactionCategoryUpdateDTO.type());
        return financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(entityToUpdate);
    }
}