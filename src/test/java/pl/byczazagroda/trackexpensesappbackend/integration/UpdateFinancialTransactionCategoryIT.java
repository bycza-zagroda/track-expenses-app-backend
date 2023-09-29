package pl.byczazagroda.trackexpensesappbackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthService;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateFinancialTransactionCategoryIT extends BaseIntegrationTestIT {

    private static final String ENDPOINT_CATEGORIES_PATCH = "/api/categories/{id}";

    private static final String CATEGORY_NAME = "Category";

    @Autowired
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    private AuthRepository userRepository;

    @Autowired
    private AuthService authService;


    @BeforeEach
    void clearDatabase() {
        financialTransactionCategoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should update FT category when user is owner and category exists")
    @Test
    void testUpdateFTCategory_whenUserIsOwnerCategory_thenShouldReturnStatusOK() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO(CATEGORY_NAME, FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(financialTransactionCategory.getId()),
                jsonPath("$.name").value(financialTransactionCategoryUpdateDTO.name()),
                jsonPath("$.type").value(financialTransactionCategoryUpdateDTO.type().toString()),
                jsonPath("$.userId").value(user.getId()));
    }

    @DisplayName("Should not update FT category when category name length > 30")
    @Test
    void testUpdateFTCategoryFailure_whenCategoryNameIsTooLong_thenReturnBadRequest() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);
        String categoryNameTooLong = "ThisIsVeryLongNameForCategoryMoreThan30Characters";
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO(categoryNameTooLong, FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpectAll(
                status().is5xxServerError(),
                jsonPath("$.status").value(ErrorCode.TEA004.getBusinessStatus()),
                jsonPath("$.message").value(ErrorCode.TEA004.getBusinessMessage()),
                jsonPath("$.statusCode").value(ErrorCode.TEA004.getBusinessStatusCode()));
    }

    @DisplayName("Should not update FT category when category name is empty")
    @Test
    void testUpdateFTCategoryFailure_whenCategoryNameIsEmpty_thenReturnBadRequest() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO("", FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()),
                jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()),
                jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));
    }

    @DisplayName("Should not update FT category when category name contains invalid characters")
    @Test
    void testUpdateFTCategoryFailure_whenCategoryNameContainsInvalidCharacters_thenReturnBadRequest() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO("!@`$%^&*()_+|-=[];',./{}:<", FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpectAll(
                status().is5xxServerError(),
                jsonPath("$.status").value(ErrorCode.TEA004.getBusinessStatus()),
                jsonPath("$.message").value(ErrorCode.TEA004.getBusinessMessage()),
                jsonPath("$.statusCode").value(ErrorCode.TEA004.getBusinessStatusCode()));
    }

    @DisplayName("Should not update FT category when category type is invalid")
    @Test
    void testUpdateFTCategoryFailure_whenCategoryTypeIsInvalid_thenReturnBadRequest() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);
        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);

        Map<String, String> categoryMap = Map.of("name", "TEST", "type", "INVALID_CATEGORY_TYPE");

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(generateJSON(categoryMap))
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()),
                jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()),
                jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));
    }

    @DisplayName("Should not update FT category when category doesn't exists")
    @Test
    void testUpdateFTCategory_whenCategoryNotExists_thenReturnIsNotFound() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String accessToken = authService.createAccessToken(user);

        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO(CATEGORY_NAME, FinancialTransactionType.EXPENSE);

        final Long categoryId = 999L;
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, categoryId)
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @DisplayName("Should not update FT category when user is not owner")
    @Test
    void testUpdateFTCategory_whenUserIsNotOwner_thenReturnIsNotFound() throws Exception {
        User user1 = userRepository.save(TestUtils.createUserWithEmailForTest(null, "user1@server.com"));
        User user2 = userRepository.save(TestUtils.createUserWithEmailForTest(null, "user2@server.com"));

        String accessToken = authService.createAccessToken(user1);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user2);

        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO(CATEGORY_NAME, FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    private FinancialTransactionCategory createFinancialTransactionCategory(User user) {
        final FinancialTransactionCategory financialTransactionCategory = FinancialTransactionCategory
                .builder()
                .name(CATEGORY_NAME + user.getId())
                .type(FinancialTransactionType.INCOME)
                .user(user)
                .build();

        return financialTransactionCategoryRepository.save(financialTransactionCategory);
    }

    private String generateJSON(Map<String, String> fromValue) {
        return (new ObjectMapper()).valueToTree(fromValue).toString();
    }

}
