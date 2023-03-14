package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialTransactionCategoryServiceImplTest {

    @Mock
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @InjectMocks
    private FinancialTransactionCategoryServiceImpl financialTransactionCategoryService;

    @Mock
    private FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;

    @Test
    @DisplayName("when finding financial transaction categories should successfully return categoryDTOs list")
    void shouldSuccessfullyFindFinancialTransactionCategories() {
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
