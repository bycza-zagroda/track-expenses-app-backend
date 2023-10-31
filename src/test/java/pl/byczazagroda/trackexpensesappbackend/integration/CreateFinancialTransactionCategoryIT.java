package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;


class CreateFinancialTransactionCategoryIT extends BaseIntegrationTestIT {

    public static final long USER_ID_1L = 1L;

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthRepository userRepository;


    @BeforeEach
    void clearDatabase() {
        financialTransactionRepository.deleteAll();
        financialTransactionCategoryRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should create financial transaction category and return status isCreated when valid data is provided")
    @Test
    void createCategory_ValidDataGiven_ShouldReturnStatusIsCreated()
            throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        var financialTransactionCategoryCreateDTO
                = new FinancialTransactionCategoryCreateDTO("Category",
                FinancialTransactionType.INCOME);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(user.getId())))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.jsonPath("$.name").value("Category"),
                        MockMvcResultMatchers.jsonPath("$.type").value("INCOME")
                );

        Assertions.assertEquals(1, financialTransactionCategoryRepository.count());
    }

    @DisplayName("Should return bad request status when creating category with name longer than 30 characters")
    @Test
    void createCategory_NameExceedingMaxLengthGiven_ShouldReturnStatusBadRequest()
            throws Exception {
        var financialTransactionCategoryCreateDTO
                = new FinancialTransactionCategoryCreateDTO("ThisIsVeryLongNameForCategoryMoreThan30Characters",
                FinancialTransactionType.INCOME);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(Long.toString(USER_ID_1L))))
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ErrorCode.TEA003.getBusinessStatus()),
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ErrorCode.TEA003.getBusinessMessage()),
                        MockMvcResultMatchers.jsonPath("$.statusCode")
                                .value(ErrorCode.TEA003.getBusinessStatusCode())
                );

        Assertions.assertEquals(0, financialTransactionCategoryRepository.count());
    }

    @DisplayName("Should return bad request status when creating category with empty name")
    @Test
    void createCategory_EmptyNameGiven_ShouldReturnStatusBadRequest() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO("",
                FinancialTransactionType.INCOME);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(Long.toString(USER_ID_1L)))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ErrorCode.TEA003.getBusinessStatus()),
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ErrorCode.TEA003.getBusinessMessage()),
                        MockMvcResultMatchers.jsonPath("$.statusCode")
                                .value(ErrorCode.TEA003.getBusinessStatusCode())
                );
    }

    @DisplayName("Should return bad request status when creating category with name containing illegal characters")
    @Test
    void createCategory_IllegalCharactersInNameGiven_ShouldReturnStatusBadRequest()
            throws Exception {
        var categoryNameForWrongPathTest = "`-'+=|\\/?,.<>%&(){}[];:" + "\"";
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO(
                categoryNameForWrongPathTest,
                FinancialTransactionType.INCOME);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(Long.toString(USER_ID_1L))))
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ErrorCode.TEA003.getBusinessStatus()),
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ErrorCode.TEA003.getBusinessMessage()),
                        MockMvcResultMatchers.jsonPath("$.statusCode")
                                .value(ErrorCode.TEA003.getBusinessStatusCode())
                );
    }

    @DisplayName("Should return bad request status when creating category without specifying a type")
    @Test
    void createCategory_NullTypeGiven_ShouldReturnStatusBadRequest() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO(
                "Category",
                null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(Long.toString(USER_ID_1L))))
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ErrorCode.TEA003.getBusinessStatus()),
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ErrorCode.TEA003.getBusinessMessage()),
                        MockMvcResultMatchers.jsonPath("$.statusCode")
                                .value(ErrorCode.TEA003.getBusinessStatusCode())
                );
    }

}
