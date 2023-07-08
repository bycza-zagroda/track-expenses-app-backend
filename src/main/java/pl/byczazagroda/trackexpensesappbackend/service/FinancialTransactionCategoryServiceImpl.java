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
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

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

    private final UserRepository userRepository;

    @Override
    public FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO,
            Long userId) {

        User user = getUserByUserId(financialTransactionCategoryCreateDTO.userId());

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
                        String.format("Financial transaction category with id: %d not found for user ", categoryId)));

        BigInteger numberOfFinancialTransactions =
                financialTransactionRepository.countFinancialTransactionsByFinancialTransactionCategoryId(categoryId);

        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory);

        return new FinancialTransactionCategoryDetailedDTO(financialTransactionCategoryDTO, numberOfFinancialTransactions);
    }


    @Override
    public List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories(Long userId) {

        List<FinancialTransactionCategory> financialTransactionCategories = financialTransactionCategoryRepository
                .findAllByUserId(userId)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format("Financial transaction categories not found for user with id: %d", userId)));

        return financialTransactionCategories.stream()
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

        User user = getUserByUserId(financialTransactionCategoryUpdateDTO.userId());

        financialTransactionCategory.setName(financialTransactionCategoryUpdateDTO.name());
        financialTransactionCategory.setType(financialTransactionCategoryUpdateDTO.type());
        financialTransactionCategory.setUser(user);

        return financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory);
    }

    private User getUserByUserId(Long userId) {
        return  userRepository.findById(userId).orElseThrow(() ->
                new AppRuntimeException(ErrorCode.U005, String.format("User with id: %d doesn't exist.", userId)));
    }

}

