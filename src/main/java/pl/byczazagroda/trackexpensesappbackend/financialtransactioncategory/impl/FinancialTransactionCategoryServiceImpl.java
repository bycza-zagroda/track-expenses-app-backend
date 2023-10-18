package pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryService;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDetailedDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.general.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;

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

    public static final String CATEGORY_WITH_ID_NOT_FOUND_FOR_USER = "Financial transaction category with id: %d not found for user ";

    private final FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    private final FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;

    private final FinancialTransactionRepository financialTransactionRepository;

    private final AuthRepository userRepository;

    @Override
    public FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO,
            Long userId) {

        User user = getUserByUserId(userId);

        FinancialTransactionCategory financialTransactionCategory = FinancialTransactionCategory
                .builder().name(financialTransactionCategoryCreateDTO.name()).type(financialTransactionCategoryCreateDTO
                        .type()).user(user).build();

        FinancialTransactionCategory savedEntity = financialTransactionCategoryRepository
                .save(financialTransactionCategory);

        return financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(savedEntity);
    }

    @Override
    public FinancialTransactionCategoryDetailedDTO findCategoryForUser(@Min(1) @NotNull Long categoryId, Long userId) {

        FinancialTransactionCategory financialTransactionCategory = financialTransactionCategoryRepository
                .findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format(CATEGORY_WITH_ID_NOT_FOUND_FOR_USER, categoryId)));

        BigInteger numberOfFinancialTransactions =
                financialTransactionRepository.countFinancialTransactionsByFinancialTransactionCategoryId(categoryId);

        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory);

        return new FinancialTransactionCategoryDetailedDTO(
                financialTransactionCategoryDTO, numberOfFinancialTransactions);
    }


    @Override
    public List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories(Long userId) {

        List<FinancialTransactionCategory> financialTransactionCategories = financialTransactionCategoryRepository
                .findAllByUserId(userId)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format("Financial transaction categories not found for user with id: %d", userId)));

        return financialTransactionCategories.stream()
                .map(financialTransactionCategoryModelMapper
                        ::mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO)
                .toList();
    }


    @Override
    public void deleteFinancialTransactionCategory(@Min(1) @NotNull Long categoryId, Long userId) {
        if (financialTransactionCategoryRepository.existsByIdAndUserId(categoryId, userId)) {
            financialTransactionCategoryRepository.deleteById(categoryId);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.FTC001,
                    String.format(CATEGORY_WITH_ID_NOT_FOUND_FOR_USER, categoryId));
        }
    }

    @Transactional
    public FinancialTransactionCategoryDTO updateFinancialTransactionCategory(
            Long categoryId, Long userId, FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO) {
        FinancialTransactionCategory financialTransactionCategory
                = financialTransactionCategoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format(CATEGORY_WITH_ID_NOT_FOUND_FOR_USER, categoryId)));

        User user = getUserByUserId(userId);

        financialTransactionCategory.setName(financialTransactionCategoryUpdateDTO.name());
        financialTransactionCategory.setType(financialTransactionCategoryUpdateDTO.type());
        financialTransactionCategory.setUser(user);

        return financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory);
    }

    private User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new AppRuntimeException(ErrorCode.U005, String.format("User with id: %d doesn't exist.", userId)));
    }

}
