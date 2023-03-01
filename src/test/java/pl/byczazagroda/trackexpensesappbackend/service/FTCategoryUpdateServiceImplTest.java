package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;

import java.time.Instant;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FTCategoryUpdateServiceImplTest {

    private static final long VALID_ID = 1L;

    private static final long INVALID_ID = 10L;

    private static final String NAME = "test_name";

    private static final FinancialTransactionType TYPE_INCOME = FinancialTransactionType.INCOME;

    private static final FinancialTransactionCategory VALID_CATEGORY
            = FinancialTransactionCategory.builder()
            .id(VALID_ID)
            .name(NAME)
            .type(TYPE_INCOME)
            .creationDate(Instant.now())
            .build();

    private static final FinancialTransactionCategoryDTO VALID_CATEGORY_DTO
            = new FinancialTransactionCategoryDTO(VALID_ID,NAME, TYPE_INCOME);

    private static final FinancialTransactionCategoryUpdateDTO VALID_UPDATE_CATEGORY_DTO
            = new FinancialTransactionCategoryUpdateDTO(NAME, TYPE_INCOME);

    @Mock
    private FinancialTransactionCategoryRepository repository;

    @Mock
    private FinancialTransactionCategoryModelMapper mapper;

    @InjectMocks
    private FinancialTransactionCategoryServiceImpl service;

    @BeforeEach
    void setUp(){
        Mockito.when(repository.findById(VALID_ID)).thenReturn(Optional.of(VALID_CATEGORY));
        Mockito.when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());
        Mockito.when(repository.save(VALID_CATEGORY)).thenReturn(VALID_CATEGORY);
        Mockito.when(mapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(ArgumentMatchers.any())).thenReturn(VALID_CATEGORY_DTO);
    }

    @Test
    @DisplayName("update the FT category and return the category object if the ID is correct")
    void testUpdateFTCategoryById_WhenIdIsCorrect_ThenReturnCategoryEntity(){
        FinancialTransactionCategoryDTO dto
                = service.updateFinancialTransactionCategory(VALID_ID,VALID_UPDATE_CATEGORY_DTO);
        Assertions.assertEquals(dto,VALID_CATEGORY_DTO);
        Mockito.verify(repository, Mockito.times(1)).save(VALID_CATEGORY);
    }

    @Test
    @DisplayName("do not update FT Category if id is incorrect instead throw AppRuntimeException and does not update entity")
    void testUpdateFTCategoryById_WhenIdIsIncorrect_ThenThrowError(){
        AppRuntimeException exception = Assertions.assertThrows(
                AppRuntimeException.class,
                ()->service.updateFinancialTransactionCategory(INVALID_ID,VALID_UPDATE_CATEGORY_DTO)
        );
        Assertions.assertEquals(ErrorCode.FTC001.getBusinessStatusCode(),exception.getBusinessStatusCode());
        Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("do not update FT Category if it is assigned to any financial transactions")
    void testUpdateFTCategoryById_WhenFTCisAssignedToFinancialTransaction_ThenThrowAnError(){
//        TODO implement test
    }

}
