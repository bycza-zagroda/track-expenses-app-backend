package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDetailedDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinancialTransactionCategoryServiceImplTest {

    public static final long ID_1L = 1L;
    public static final long ID_2L = 2L;
    public static final long ID_3L = 3L;
    public static final String CATEGORY_NAME = "Name";
    public static final String CATEGORY_NAME_UPDATE = "UpdatedName";
    public static final FinancialTransactionType CATEGORY_TYPE = FinancialTransactionType.EXPENSE;
    public static final BigInteger TRANSACTION_COUNTER = new BigInteger("5");

    @Mock
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @InjectMocks
    private FinancialTransactionCategoryServiceImpl financialTransactionCategoryService;

    @Mock
    private FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;

    @Mock
    private FinancialTransactionRepository financialTransactionRepository;


    @Test
    @DisplayName("create financial transaction category when valid parameters are provided")
    void shouldSuccessfullyCreateTransactionCategory_whenValidParametersProvided_thenReturnFinancialTransactionCategoryDTO() {
        //given
        FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO(
                CATEGORY_NAME, CATEGORY_TYPE);
        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory();
        when(financialTransactionCategoryRepository.save(any())).thenReturn(financialTransactionCategory);
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = new FinancialTransactionCategoryDTO(ID_1L, CATEGORY_NAME, CATEGORY_TYPE);
        when(financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(any())).thenReturn(financialTransactionCategoryDTO);

        //when
        FinancialTransactionCategoryDTO fTCResult = financialTransactionCategoryService.createFinancialTransactionCategory(financialTransactionCategoryCreateDTO);

        //then
        assertEquals(financialTransactionCategoryDTO, fTCResult);
    }

    @Test
    @DisplayName("when providing proper transaction category id, it should successfully find trasnaction category")
    void shouldSuccessfullyFindTransactionCategory_WhenProvidingProperTransactionCategoryId() {
        //given
        FinancialTransactionCategory financialTransactionCategory = new FinancialTransactionCategory(CATEGORY_NAME, CATEGORY_TYPE);
        financialTransactionCategory.setId(ID_1L);
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = new FinancialTransactionCategoryDTO(ID_1L, CATEGORY_NAME, CATEGORY_TYPE);
        FinancialTransactionCategoryDetailedDTO financialTransactionCategoryDetailedDTO = new FinancialTransactionCategoryDetailedDTO(financialTransactionCategoryDTO, TRANSACTION_COUNTER);
        //when
        when(financialTransactionCategoryRepository.findById(ID_1L)).thenReturn(Optional.of(financialTransactionCategory));
        when(financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory))
                .thenReturn(financialTransactionCategoryDTO);
        when(financialTransactionRepository.countByType(CATEGORY_TYPE)).thenReturn(5L);

        FinancialTransactionCategoryDetailedDTO foundTransactionCategoryDetailedDTO = financialTransactionCategoryService.getFinancialTransactionCategoryById(ID_1L);
        //then
        Assertions.assertEquals(financialTransactionCategoryDetailedDTO, foundTransactionCategoryDetailedDTO);
    }

    @Test
    @DisplayName("when providing wrong transaction category id, it should throw AppRuntimeException")
    void shouldNotFindTransactionCategoryWhenIdDoesNotExist_ThenThrowAppRuntimeException() {
        //given

        //when
        when(financialTransactionCategoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        assertThrows(AppRuntimeException.class, () -> financialTransactionCategoryService.getFinancialTransactionCategoryById(ID_3L));
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(() -> financialTransactionCategoryService.getFinancialTransactionCategoryById(ID_3L))
                .withMessage(ErrorCode.FTC001.getBusinessMessage());
    }

    @Test
    @DisplayName("should display all available transaction categories")
    void shouldFindAllTransactionCategories() {
        //given
        FinancialTransactionCategory financialTransactionCategory1 = createFinancialTransactionCategory();
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO1 = new FinancialTransactionCategoryDTO(ID_1L, CATEGORY_NAME, CATEGORY_TYPE);

        FinancialTransactionCategory financialTransactionCategory2 = new FinancialTransactionCategory(CATEGORY_NAME, CATEGORY_TYPE);
        financialTransactionCategory2.setId(ID_2L);
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO2 = new FinancialTransactionCategoryDTO(ID_2L, CATEGORY_NAME, CATEGORY_TYPE);

        List<FinancialTransactionCategory> financialTransactionCategoryList = new ArrayList<>();
        financialTransactionCategoryList.add(financialTransactionCategory1);
        financialTransactionCategoryList.add(financialTransactionCategory2);

        //when
        when(financialTransactionCategoryRepository.findAll()).thenReturn(financialTransactionCategoryList);
        when(financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory1))
                .thenReturn(financialTransactionCategoryDTO1);
        when(financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory2))
                .thenReturn(financialTransactionCategoryDTO2);

        List<FinancialTransactionCategoryDTO> financialTransactionCategoryDTOSResult = financialTransactionCategoryService.getFinancialTransactionCategories();
        //then
        Assertions.assertEquals(financialTransactionCategoryDTOSResult.get(0), financialTransactionCategoryDTO1);
        Assertions.assertEquals(financialTransactionCategoryDTOSResult.get(1), financialTransactionCategoryDTO2);
    }

    @Test
    @DisplayName("when trying to delete transaction category that doesnt exist, should throw an AppRuntimeException")
    void shouldThrowAnAppRuntimeException_WhenFindingTransactionCategoryToDeleteWithIdThatDoesNotExist() {
        //given

        //when
        Mockito.lenient().when(financialTransactionCategoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        assertThrows(AppRuntimeException.class, () -> financialTransactionCategoryService.deleteFinancialTransactionCategoryById(ID_3L));
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(() -> financialTransactionCategoryService.deleteFinancialTransactionCategoryById(ID_3L))
                .withMessage(ErrorCode.FTC001.getBusinessMessage());
    }

    @Test
    @DisplayName("should update record based on provided parameters in transaction creation DTO, when Id is found in database")
    void shouldReturnUpdatedTransactionCategoryDTO_WhenTransactionCategoryCreateHasValidParameters_And_IdExistInDatabase() {
        //given
        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory();
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO = new FinancialTransactionCategoryUpdateDTO(CATEGORY_NAME_UPDATE, CATEGORY_TYPE);
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = new FinancialTransactionCategoryDTO(ID_1L, CATEGORY_NAME_UPDATE, CATEGORY_TYPE);
        //when
        when(financialTransactionCategoryRepository.findById(ID_1L)).thenReturn(Optional.of(financialTransactionCategory));
        when(financialTransactionCategoryModelMapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory))
                .thenReturn(financialTransactionCategoryDTO);

        FinancialTransactionCategoryDTO ftcResult = financialTransactionCategoryService.updateFinancialTransactionCategory(ID_1L, financialTransactionCategoryUpdateDTO);
        //then
        Assertions.assertEquals(ftcResult.name(), CATEGORY_NAME_UPDATE);
    }

    @Test
    @DisplayName("when finding transaction to update by Id that does not exist in the database, should thrown an AppRuntimeException")
    void shouldThrowAnAppRuntimeException_WhenFindingTransactionCategoryToUpdateWithIdThatDoesNotExist() {
        //given
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO = new FinancialTransactionCategoryUpdateDTO(CATEGORY_NAME, FinancialTransactionType.INCOME);

        //when
        Mockito.lenient().when(financialTransactionCategoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        assertThrows(AppRuntimeException.class, () -> financialTransactionCategoryService.updateFinancialTransactionCategory(ID_1L, financialTransactionCategoryUpdateDTO));
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(() -> financialTransactionCategoryService.updateFinancialTransactionCategory(ID_1L, financialTransactionCategoryUpdateDTO))
                .withMessage(ErrorCode.FTC001.getBusinessMessage());
    }

    private FinancialTransactionCategory createFinancialTransactionCategory() {
        FinancialTransactionCategory financialTransactionCategory = new FinancialTransactionCategory(CATEGORY_NAME, CATEGORY_TYPE);
        financialTransactionCategory.setId(ID_1L);
        return financialTransactionCategory;
    }
}