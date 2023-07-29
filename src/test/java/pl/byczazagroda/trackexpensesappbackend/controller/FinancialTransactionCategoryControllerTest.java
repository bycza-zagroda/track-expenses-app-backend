package pl.byczazagroda.trackexpensesappbackend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.config.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionCategoryService;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionCategoryServiceImpl;
import pl.byczazagroda.trackexpensesappbackend.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FinancialTransactionCategoryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = FinancialTransactionCategoryServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {FinancialTransactionCategoryModelMapper.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
class FinancialTransactionCategoryControllerTest {

    private static final Long USER_ID_1L = 1L;

    private static final Long FINANCIAL_TRANSACTION_CATEGORY_ID_1L = 1L;

    private static final Long FINANCIAL_TRANSACTION_CATEGORY_ID_2L = 2L;

    private static final Long FINANCIAL_TRANSACTION_CATEGORY_ID_3L = 3L;

    private static final String FINANCIAL_TRANSACTION_CATEGORY_NAME_FIRST = "First";

    private static final String FINANCIAL_TRANSACTION_CATEGORY_NAME_SECOND = "Second";

    private static final String FINANCIAL_TRANSACTION_CATEGORY_NAME_THIRD = "Third";

    @Autowired
    private MockMvc mockMvc;
        
    @MockBean
    private FinancialTransactionCategoryService financialTransactionCategoryService;

    @MockBean
    private UserServiceImpl userService;

    @Test
    @WithMockUser(username = "1")
    @DisplayName("when getting all financial transaction categories " +
            "should return financial transaction category DTOs list and response status OK")
    void shouldResponseStatusOKAndFinancialTransactionCategoryDTOsList() throws Exception {

        // given
        List<FinancialTransactionCategoryDTO> categoriesListDTO = createFinancialTransactionCategoryDTOList();
        given(financialTransactionCategoryService.getFinancialTransactionCategories(USER_ID_1L))
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
        FinancialTransactionCategoryDTO categoryFirstDTO = new FinancialTransactionCategoryDTO(FINANCIAL_TRANSACTION_CATEGORY_ID_1L, FINANCIAL_TRANSACTION_CATEGORY_NAME_FIRST, FinancialTransactionType.INCOME, USER_ID_1L);
        FinancialTransactionCategoryDTO categorySecondDTO = new FinancialTransactionCategoryDTO(FINANCIAL_TRANSACTION_CATEGORY_ID_2L, FINANCIAL_TRANSACTION_CATEGORY_NAME_SECOND, FinancialTransactionType.INCOME, USER_ID_1L);
        FinancialTransactionCategoryDTO categoryThirdDTO = new FinancialTransactionCategoryDTO(FINANCIAL_TRANSACTION_CATEGORY_ID_3L, FINANCIAL_TRANSACTION_CATEGORY_NAME_THIRD, FinancialTransactionType.INCOME, USER_ID_1L);

        return List.of(categoryFirstDTO, categorySecondDTO, categoryThirdDTO);       
    }
    
}
