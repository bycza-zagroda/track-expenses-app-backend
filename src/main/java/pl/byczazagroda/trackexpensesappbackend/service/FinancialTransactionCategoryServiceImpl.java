package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
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

@RequiredArgsConstructor
@Service
@Validated
public class FinancialTransactionCategoryServiceImpl implements FinancialTransactionCategoryService {

    private final FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    private final FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;

    private final FinancialTransactionRepository financialTransactionRepository;

    @Override
    public FinancialTransactionCategoryDTO createFinancialTransactionCategory(@Valid
            FinancialTransactionCategoryCreateDTO dto) {

        FinancialTransactionCategory entityToSave = new FinancialTransactionCategory(dto.name(), dto.type());
        FinancialTransactionCategory savedEntity = financialTransactionCategoryRepository.save(entityToSave);

        return financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(savedEntity);
    }

    @Override
    public FinancialTransactionCategoryDetailedDTO findById(@Min(1) @NotNull Long id) {
        FinancialTransactionCategory financialTransactionCategory = financialTransactionCategoryRepository.findById(id)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format("Financial transaction category with id: %d not found", id)));
        BigInteger numberOfFinancialTransactions =
                financialTransactionRepository.countFinancialTransactionsByFinancialTransactionCategoryId(id);
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory);

        return new FinancialTransactionCategoryDetailedDTO(financialTransactionCategoryDTO, numberOfFinancialTransactions);
    }

    @Override
    public List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories() {
        return financialTransactionCategoryRepository.findAll().stream()
                .map(financialTransactionCategoryModelMapper::mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO)
                .toList();
    }

    @Override

    public void deleteFinancialTransactionCategory(@Min(1) @NotNull Long id) {
        if (financialTransactionCategoryRepository.existsById(id)) {
            financialTransactionCategoryRepository.deleteById(id);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.FTC001,
                    String.format("Financial transaction category with given id: %d does not exist", id));
        }
    }

    @Transactional
    public FinancialTransactionCategoryDTO updateFinancialTransactionCategory(
            Long id, FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO) {
        FinancialTransactionCategory financialTransactionCategory
                = financialTransactionCategoryRepository.findById(id)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format("Financial transaction category with id: %d not found", id)));
        financialTransactionCategory.setName(financialTransactionCategoryUpdateDTO.name());
        financialTransactionCategory.setType(financialTransactionCategoryUpdateDTO.type());
        return financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory);
    }

}

