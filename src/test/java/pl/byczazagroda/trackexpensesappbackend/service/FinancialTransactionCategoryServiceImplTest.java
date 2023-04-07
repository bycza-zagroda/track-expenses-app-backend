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
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


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

    @DisplayName("create financial transaction category when valid parameters are provided")
    @Test
    void testCreateTransactionCategory_whenValidParametersProvided_thenReturnFinancialTransactionCategoryDTO() {
        //given
        FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO =
                new FinancialTransactionCategoryCreateDTO(CATEGORY_NAME, CATEGORY_TYPE);
        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory();
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

    private FinancialTransactionCategory createFinancialTransactionCategory() {
        FinancialTransactionCategory financialTransactionCategory = new FinancialTransactionCategory(CATEGORY_NAME,
                CATEGORY_TYPE);
        financialTransactionCategory.setId(ID_1L);
        return financialTransactionCategory;
    }

    @Test
    @DisplayName("when finding financial transaction categories should successfully return categoryDTOs list")
    void testReadTransactionCategories_whenExecutingFindAll_thenReturnTransactionCategoriesDTOList() {
        //given
        FinancialTransactionCategory categoryFirst = new FinancialTransactionCategory("First", FinancialTransactionType.INCOME);
        FinancialTransactionCategory categorySecond = new FinancialTransactionCategory("Second", FinancialTransactionType.INCOME);
        FinancialTransactionCategory categoryThird = new FinancialTransactionCategory("Third", FinancialTransactionType.INCOME);
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

        List<FinancialTransactionCategoryDTO> returnedFinancialTransactionCategoryDTOsList = financialTransactionCategoryService.getFinancialTransactionCategories();

        //then
        Assertions.assertEquals(returnedFinancialTransactionCategoryDTOsList.get(0), categoryFirstDTO);
        Assertions.assertEquals(returnedFinancialTransactionCategoryDTOsList.get(1), categorySecondDTO);
        Assertions.assertEquals(returnedFinancialTransactionCategoryDTOsList.get(2), categoryThirdDTO);
    }
    
}