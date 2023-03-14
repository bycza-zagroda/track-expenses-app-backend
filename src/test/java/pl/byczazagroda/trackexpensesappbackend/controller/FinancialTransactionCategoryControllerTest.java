package pl.byczazagroda.trackexpensesappbackend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionCategoryService;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionCategoryServiceImpl;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FinancialTransactionCategoryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = FinancialTransactionCategoryServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {FinancialTransactionCategoryModelMapper.class, ErrorStrategy.class}))
@ActiveProfiles("test")
class FinancialTransactionCategoryControllerTest {
        
    @Autowired
    private MockMvc mockMvc;
        
    @MockBean
    private FinancialTransactionCategoryService financialTransactionCategoryService;

    @Test
    @DisplayName("when getting all financial transaction categories " +
            "should return financial transaction category DTOs list and response status OK")
    void shouldResponseStatusOKAndFinancialTransactionCategoryDTOsList() throws Exception {

        // given
        List<FinancialTransactionCategoryDTO> categoriesListDTO = createFinancialTransactionCategoryDTOList();
        given(financialTransactionCategoryService.getFinancialTransactionCategories())
                .willReturn(categoriesListDTO);

        // when

        // then
        mockMvc.perform(get("/api/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(3));

    }

    private static List<FinancialTransactionCategoryDTO> createFinancialTransactionCategoryDTOList() {
        FinancialTransactionCategoryDTO categoryFirstDTO = new FinancialTransactionCategoryDTO(1L, "First", FinancialTransactionType.INCOME);
        FinancialTransactionCategoryDTO categorySecondDTO = new FinancialTransactionCategoryDTO(2L, "Second", FinancialTransactionType.INCOME);
        FinancialTransactionCategoryDTO categoryThirdDTO = new FinancialTransactionCategoryDTO(3L, "Third", FinancialTransactionType.INCOME);

        return List.of(categoryFirstDTO, categorySecondDTO, categoryThirdDTO);       
    }
    
}
