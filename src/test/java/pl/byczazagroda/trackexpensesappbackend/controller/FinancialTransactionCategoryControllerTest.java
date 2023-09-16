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
import pl.byczazagroda.trackexpensesappbackend.config.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategoryController;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategoryModelMapper;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.model.User;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategoryService;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategoryServiceImpl;
import pl.byczazagroda.trackexpensesappbackend.service.UserServiceImpl;

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
    private UserServiceImpl userService;

    @Test
    @WithMockUser(username = "1")
    @DisplayName("when getting all financial transaction categories "
            + "should return financial transaction category DTOs list and response status OK")
    void shouldResponseStatusOKAndFinancialTransactionCategoryDTOsList() throws Exception {

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
