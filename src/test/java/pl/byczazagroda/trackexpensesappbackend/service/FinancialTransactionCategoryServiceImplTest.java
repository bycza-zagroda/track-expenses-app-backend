package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDetailedDTO;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.impl.FinancialTransactionCategoryServiceImpl;
import pl.byczazagroda.trackexpensesappbackend.general.exception.AppRuntimeException;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinancialTransactionCategoryServiceImplTest {

    public static final String CATEGORY_NAME = "Name";
    public static final FinancialTransactionType CATEGORY_TYPE = FinancialTransactionType.EXPENSE;
    private static final long FINANCIAL_TRANSACTION_CATEGORY_ID_1L = 1L;
    private static final long FINANCIAL_TRANSACTION_CATEGORY_ID_2L = 2L;
    private static final long FINANCIAL_TRANSACTION_CATEGORY_ID_3L = 3L;
    private static final long USER_ID_1L = 1L;
    private static final String FINANCIAL_TRANSACTION_CATEGORY_NAME_FIRST = "First";
    private static final String FINANCIAL_TRANSACTION_CATEGORY_NAME_SECOND = "Second";
    private static final String FINANCIAL_TRANSACTION_CATEGORY_NAME_THIRD = "Third";
    private static final String FINANCIAL_TRANSACTION_CATEGORY_NAME_EXAMPLE_NAME = "example name";
    @Mock
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @InjectMocks
    private FinancialTransactionCategoryServiceImpl financialTransactionCategoryService;

    @Mock
    private FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;

    @Mock
    private FinancialTransactionRepository financialTransactionRepository;

    @Mock
    private AuthRepository userRepository;

    @DisplayName("create financial transaction category when valid parameters are provided")
    @Test
    void testCreateTransactionCategory_whenValidParametersProvided_thenReturnFinancialTransactionCategoryDTO() {
        //given
        FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO =
                new FinancialTransactionCategoryCreateDTO(CATEGORY_NAME, CATEGORY_TYPE);

        FinancialTransactionCategory financialTransactionCategory =
                createFinancialTransactionCategory(CATEGORY_NAME, CATEGORY_TYPE);
        financialTransactionCategory.setId(FINANCIAL_TRANSACTION_CATEGORY_ID_1L);

        when(financialTransactionCategoryRepository.save(any())).thenReturn(financialTransactionCategory);
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = new FinancialTransactionCategoryDTO(
                FINANCIAL_TRANSACTION_CATEGORY_ID_1L, CATEGORY_NAME, CATEGORY_TYPE, USER_ID_1L);
        when(financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(any()))
                .thenReturn(financialTransactionCategoryDTO);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        //when
        FinancialTransactionCategoryDTO fTCResult =
                financialTransactionCategoryService
                        .createFinancialTransactionCategory(financialTransactionCategoryCreateDTO, USER_ID_1L);

        //then
        assertEquals(financialTransactionCategoryDTO, fTCResult);
    }

    @Test
    @DisplayName("when finding financial transaction categories should successfully return categoryDTOs list")
    void testReadTransactionCategories_whenExecutingFindAll_thenReturnTransactionCategoriesDTOList() {
        //given
        User user = TestUtils.createUserForTest();
        final int categoryCounter = 3;
        List<FinancialTransactionCategory> categoryList =
                TestUtils.createFinancialTransactionCategoryListForTest(
                        categoryCounter, FinancialTransactionType.INCOME, user);

        List<FinancialTransactionCategoryDTO> categoryDTOList =
                TestUtils.createFinancialTransactionCategoryDTOListForTest(
                        categoryCounter, FinancialTransactionType.INCOME, user.getId());

        //when
        when(financialTransactionCategoryRepository.findAllByUserId(user.getId())).thenReturn(Optional.of(categoryList));
        when(financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(categoryList.get(0)))
                .thenReturn(categoryDTOList.get(0));
        when(financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(categoryList.get(1)))
                .thenReturn(categoryDTOList.get(1));
        when(financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(categoryList.get(2)))
                .thenReturn(categoryDTOList.get(2));

        List<FinancialTransactionCategoryDTO> returnedFinancialTransactionCategoryDTOsList =
                financialTransactionCategoryService.getFinancialTransactionCategories(user.getId());


        //then
        Assertions.assertEquals(categoryDTOList, returnedFinancialTransactionCategoryDTOsList);
    }

    @Test
    @DisplayName("when financial transaction category exists should delete it successfully")
    void shouldSuccessfullyDeleteFinancialTransactionCategory_WhenGivenCategoryExists() {
        //when
        when(financialTransactionCategoryRepository.existsByIdAndUserId(anyLong(), anyLong())).thenReturn(true);
        financialTransactionCategoryService.deleteFinancialTransactionCategory(FINANCIAL_TRANSACTION_CATEGORY_ID_1L,
                USER_ID_1L);

        //then
        verify(financialTransactionCategoryRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("when financial transaction category doesn't exist should throw an exception")
    void shouldFailToDeleteFinancialTransactionCategory_WhenIdNotExists() {
        //when
        when(financialTransactionCategoryRepository.existsByIdAndUserId(anyLong(), anyLong())).thenReturn(false);

        //then
        Assertions.assertThrows(AppRuntimeException.class,
                () -> financialTransactionCategoryService
                        .deleteFinancialTransactionCategory(FINANCIAL_TRANSACTION_CATEGORY_ID_1L, USER_ID_1L));
    }

    @Test
    @DisplayName("when finding with proper financial transaction category id "
            + "should successfully find financial transaction category and number of financial transaction")
    void shouldSuccessfullyFindFinancialTransactionCategory_WhenFindingWithProperFinancialTransactionCategoryId() {
        //given
        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(
                FINANCIAL_TRANSACTION_CATEGORY_NAME_EXAMPLE_NAME,
                FinancialTransactionType.INCOME);
        financialTransactionCategory.setId(FINANCIAL_TRANSACTION_CATEGORY_ID_1L);

        final BigInteger numberOfFinancialTransactions = BigInteger.valueOf(5);

        FinancialTransactionCategoryDTO financialTransactionCategoryDTO =
                new FinancialTransactionCategoryDTO(
                        FINANCIAL_TRANSACTION_CATEGORY_ID_1L,
                        FINANCIAL_TRANSACTION_CATEGORY_NAME_EXAMPLE_NAME,
                        FinancialTransactionType.INCOME,
                        USER_ID_1L);

        //when
        when(financialTransactionCategoryRepository.findByIdAndUserId(FINANCIAL_TRANSACTION_CATEGORY_ID_1L, USER_ID_1L))
                .thenReturn(Optional.of(financialTransactionCategory));
        when(financialTransactionRepository
                .countFinancialTransactionsByFinancialTransactionCategoryId(FINANCIAL_TRANSACTION_CATEGORY_ID_1L))
                .thenReturn(numberOfFinancialTransactions);
        when(financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory))
                .thenReturn(financialTransactionCategoryDTO);

        FinancialTransactionCategoryDetailedDTO foundFinancialTransactionCategory =
                financialTransactionCategoryService.findCategoryForUser(FINANCIAL_TRANSACTION_CATEGORY_ID_1L, USER_ID_1L);

        //then
        Assertions.assertEquals(financialTransactionCategoryDTO, foundFinancialTransactionCategory.financialTransactionCategoryDTO());
        Assertions.assertEquals(numberOfFinancialTransactions, foundFinancialTransactionCategory.financialTransactionsCounter());
    }

    @Test
    @DisplayName("when financial transaction category doesn't exist should throw an exception")
    void shouldFailToReadFinancialTransactionCategoryById_WhenIdDoNotExists() {
        //when
        when(financialTransactionCategoryRepository
                .findByIdAndUserId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(AppRuntimeException.class,
                () -> financialTransactionCategoryService.findCategoryForUser(1L, 1L));
    }

    private FinancialTransactionCategory createFinancialTransactionCategory(String name, FinancialTransactionType type) {
        return FinancialTransactionCategory.builder()
                .name(name)
                .type(type)
                .creationDate(Instant.now())
                .build();
    }

}
