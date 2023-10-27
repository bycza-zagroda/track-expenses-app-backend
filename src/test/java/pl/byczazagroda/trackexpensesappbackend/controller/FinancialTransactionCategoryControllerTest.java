package pl.byczazagroda.trackexpensesappbackend.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.auth.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.FinancialTransactionCategoryController;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryService;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.impl.FinancialTransactionCategoryServiceImpl;
import pl.byczazagroda.trackexpensesappbackend.auth.impl.AuthServiceImpl;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FinancialTransactionCategoryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = FinancialTransactionCategoryServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {FinancialTransactionCategoryModelMapper.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
class FinancialTransactionCategoryControllerTest {

    private static final Long USER_ID_1L = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinancialTransactionCategoryService financialTransactionCategoryService;

    @MockBean
    private AuthServiceImpl userService;

    @Test
    @WithMockUser(username = "1")
    @DisplayName("Should return financial transaction categories with status OK")
    void getFinancialTransactionCategories_AllCategoriesExist_ShouldReturnCategoryDTOsListAndStatusOk()
            throws Exception {

        // given
        final int transactionCounter = 4;
        User user = TestUtils.createUserForTest(1L);

        List<FinancialTransactionCategoryDTO> categoriesListDTO =
                TestUtils.createFinancialTransactionCategoryDTOListForTest(
                        transactionCounter,
                        FinancialTransactionType.INCOME,
                        user.getId());

        given(financialTransactionCategoryService.getFinancialTransactionCategories(USER_ID_1L))
                .willReturn(categoriesListDTO);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/categories")
                .accept(MediaType.APPLICATION_JSON));
        // then
        Assertions.assertAll(
                () -> resultActions
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(transactionCounter)),
                () -> Mockito.verify(financialTransactionCategoryService, Mockito.times(1))
                        .getFinancialTransactionCategories(USER_ID_1L)
        );
    }

}
