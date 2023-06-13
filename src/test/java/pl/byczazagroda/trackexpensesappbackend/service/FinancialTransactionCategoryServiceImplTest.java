package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDetailedDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;


@ExtendWith(MockitoExtension.class)
public class FinancialTransactionCategoryServiceImplTest {

    public static final long ID_1L = 1L;

    public static final String CATEGORY_NAME = "Name";

    public static final FinancialTransactionType CATEGORY_TYPE = FinancialTransactionType.EXPENSE;


    @Mock
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @InjectMocks
    private FinancialTransactionCategoryServiceImpl financialTransactionCategoryService;

    @Mock
    private FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;

    @Mock
    private FinancialTransactionRepository financialTransactionRepository;

    @Mock
    private UserRepository userRepository;

    @DisplayName("create financial transaction category when valid parameters are provided")
    @Test
    void testCreateTransactionCategory_whenValidParametersProvided_thenReturnFinancialTransactionCategoryDTO() {
        //given
        FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO =
                new FinancialTransactionCategoryCreateDTO(CATEGORY_NAME, CATEGORY_TYPE);
        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(CATEGORY_NAME, CATEGORY_TYPE);
        financialTransactionCategory.setId(ID_1L);
        when(financialTransactionCategoryRepository.save(any())).thenReturn(financialTransactionCategory);
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = new FinancialTransactionCategoryDTO(ID_1L,
                CATEGORY_NAME, CATEGORY_TYPE);
        when(financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(any())).thenReturn(financialTransactionCategoryDTO);

        //when
        FinancialTransactionCategoryDTO fTCResult =
                financialTransactionCategoryService.createFinancialTransactionCategory(financialTransactionCategoryCreateDTO);
        //then
        assertEquals(financialTransactionCategoryDTO, fTCResult);
    }

    private FinancialTransactionCategory createFinancialTransactionCategory(String name, FinancialTransactionType type) {
        return FinancialTransactionCategory.builder()
                .name(name)
                .type(type)
                .creationDate(Instant.now())
                .build();
    }

    @Test
    @DisplayName("when finding financial transaction categories should successfully return categoryDTOs list")
    void testReadTransactionCategories_whenExecutingFindAll_thenReturnTransactionCategoriesDTOList() {
        //given
        FinancialTransactionCategory categoryFirst = createFinancialTransactionCategory("First", FinancialTransactionType.INCOME);
        FinancialTransactionCategory categorySecond = createFinancialTransactionCategory("Second", FinancialTransactionType.INCOME);
        FinancialTransactionCategory categoryThird = createFinancialTransactionCategory("Third", FinancialTransactionType.INCOME);
        FinancialTransactionCategoryDTO categoryFirstDTO = new FinancialTransactionCategoryDTO(1L, "First", FinancialTransactionType.INCOME);
        FinancialTransactionCategoryDTO categorySecondDTO = new FinancialTransactionCategoryDTO(2L, "Second", FinancialTransactionType.INCOME);
        FinancialTransactionCategoryDTO categoryThirdDTO = new FinancialTransactionCategoryDTO(3L, "Third", FinancialTransactionType.INCOME);
        List<FinancialTransactionCategory> categoryList = new ArrayList<>();
        categoryList.add(categoryFirst);
        categoryList.add(categorySecond);
        categoryList.add(categoryThird);

        //when
        when(financialTransactionCategoryRepository.findAll()).thenReturn(categoryList);
        when(financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(categoryFirst))
                .thenReturn(categoryFirstDTO);
        when(financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(categorySecond))
                .thenReturn(categorySecondDTO);
        when(financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(categoryThird))
                .thenReturn(categoryThirdDTO);

        List<FinancialTransactionCategoryDTO> returnedFinancialTransactionCategoryDTOsList =
                financialTransactionCategoryService.getFinancialTransactionCategories();

        //then
        Assertions.assertEquals(returnedFinancialTransactionCategoryDTOsList.get(0), categoryFirstDTO);
        Assertions.assertEquals(returnedFinancialTransactionCategoryDTOsList.get(1), categorySecondDTO);
        Assertions.assertEquals(returnedFinancialTransactionCategoryDTOsList.get(2), categoryThirdDTO);
    }

    @Test
    @DisplayName("when financial transaction category exists should delete it successfully")
    void shouldSuccessfullyDeleteFinancialTransactionCategory_WhenGivenCategoryExists() {
        //when
        when(financialTransactionCategoryRepository.existsById(anyLong())).thenReturn(true);
        financialTransactionCategoryService.deleteFinancialTransactionCategory(ID_1L);

        //then
        verify(financialTransactionCategoryRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("when financial transaction category doesn't exist should throw an exception")
    void shouldFailToDeleteFinancialTransactionCategory_WhenIdNotExists() {
        //when
        when(financialTransactionCategoryRepository.existsById(anyLong())).thenReturn(false);

        //then
        Assertions.assertThrows(AppRuntimeException.class,
                () -> financialTransactionCategoryService.deleteFinancialTransactionCategory(ID_1L));
    }

    @Test
    @DisplayName("when finding with proper financial transaction category id " +
            "should successfully find financial transaction category and number of financial transaction")
    void shouldSuccessfullyFindFinancialTransactionCategory_WhenFindingWithProperFinancialTransactionCategoryId() {
        //given
        Long id = 1L;
        FinancialTransactionCategory financialTransactionCategory = new FinancialTransactionCategory();
        financialTransactionCategory.setId(id);
        financialTransactionCategory.setName("example name");
        financialTransactionCategory.setType(FinancialTransactionType.INCOME);
        BigInteger numberOfFinancialTransactions = BigInteger.valueOf(5);

        FinancialTransactionCategoryDTO financialTransactionCategoryDTO =
                new FinancialTransactionCategoryDTO(id, "example name", FinancialTransactionType.INCOME);

        //when
        when(financialTransactionCategoryRepository.findById(id)).thenReturn(Optional.of(financialTransactionCategory));
        when(financialTransactionRepository.countFinancialTransactionsByFinancialTransactionCategoryId(id))
                .thenReturn(numberOfFinancialTransactions);
        when(financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory))
                .thenReturn(financialTransactionCategoryDTO);

        FinancialTransactionCategoryDetailedDTO foundFinancialTransactionCategory =
                financialTransactionCategoryService.findById(id);

        //then
        Assertions.assertEquals(financialTransactionCategoryDTO,
                foundFinancialTransactionCategory.financialTransactionCategoryDTO());
        Assertions.assertEquals(numberOfFinancialTransactions,
                foundFinancialTransactionCategory.financialTransactionsCounter());
    }

    @Test
    @DisplayName("when financial transaction category doesn't exist should throw an exception")
    void shouldFailToReadFinancialTransactionCategoryById_WhenIdDoNotExists() {
        //when
        when(financialTransactionCategoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(AppRuntimeException.class, () -> financialTransactionCategoryService.findById(1L));
    }

}