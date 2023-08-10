package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateFinancialTransactionCategoryIT extends BaseIntegrationTestIT {

    private static final String ENDPOINT_CATEGORIES_PATCH = "/api/categories/{id}";

    private static final String CATEGORY_NAME = "Category";

    @Autowired
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @BeforeEach
    void clearDatabase() {
        financialTransactionCategoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should update FT category when user is owner and category exists")
    @Test
    void testUpdateFTCategory_whenUserIsOwnerCategory_thenShouldReturnStatusOK() throws Exception {
        User user = createUser("user@domain.server.com");
        String accessToken = userService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO(CATEGORY_NAME, FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(financialTransactionCategory.getId()),
                jsonPath("$.name").value(financialTransactionCategoryUpdateDTO.name()),
                jsonPath("$.type").value(financialTransactionCategoryUpdateDTO.type().toString()),
                jsonPath("$.userId").value(user.getId())
        );
    }

    @DisplayName("Should not update FT category when category name length > 30")
    @Test
    void testUpdateFTCategoryFailure_whenCategoryNameIsTooLong_thenReturnBadRequest() throws Exception {
        User user = createUser("user@domain.server.com");
        String accessToken = userService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);
        String categoryNameTooLong = "ThisIsVeryLongNameForCategoryMoreThan30Characters";
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO(categoryNameTooLong, FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpectAll(
                status().is5xxServerError(),
                jsonPath("$.status").value(ErrorCode.TEA004.getBusinessStatus()),
                jsonPath("$.message").value(ErrorCode.TEA004.getBusinessMessage()),
                jsonPath("$.statusCode").value(ErrorCode.TEA004.getBusinessStatusCode())
        );
    }

    @DisplayName("Should not update FT category when category name is empty")
    @Test
    void testUpdateFTCategoryFailure_whenCategoryNameIsEmpty_thenReturnBadRequest() throws Exception {
        User user = createUser("user@domain.server.com");
        String accessToken = userService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO("", FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()),
                jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()),
                jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @DisplayName("Should not update FT category when category name contains invalid characters")
    @Test
    void testUpdateFTCategoryFailure_whenCategoryNameContainsInvalidCharacters_thenReturnBadRequest() throws Exception {
        User user = createUser("user@domain.server.com");
        String accessToken = userService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);
        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO("!@`$%^&*()_+|-=[];',./{}:<", FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpectAll(
                status().is5xxServerError(),
                jsonPath("$.status").value(ErrorCode.TEA004.getBusinessStatus()),
                jsonPath("$.message").value(ErrorCode.TEA004.getBusinessMessage()),
                jsonPath("$.statusCode").value(ErrorCode.TEA004.getBusinessStatusCode())
        );
    }

    @DisplayName("Should not update FT category when category type is invalid")
    @Test
    void testUpdateFTCategoryFailure_whenCategoryTypeIsInvalid_thenReturnBadRequest() throws Exception {
        User user = createUser("user@domain.server.com");
        String accessToken = userService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"TEST\", \"type\": \"INVALID_CATEGORY_TYPE\"}")
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()),
                jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()),
                jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @DisplayName("Should not update FT category when category doesn't exists")
    @Test
    void testUpdateFTCategory_whenCategoryNotExists_thenReturnIsNotFound() throws Exception {
        User user = createUser("user@domain.server.com");
        String accessToken = userService.createAccessToken(user);

        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO(CATEGORY_NAME, FinancialTransactionType.EXPENSE);

        Long categoryId = 999L;
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, categoryId)
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isNotFound());
    }

    @DisplayName("Should not update FT category when user is not owner")
    @Test
    void testUpdateFTCategory_whenUserIsNotOwner_thenReturnIsNotFound() throws Exception {
        User user = createUser("user@domain.server.com");
        User user2 = createUser("user2@domain.server.com");
        String accessToken = userService.createAccessToken(user);

        FinancialTransactionCategory financialTransactionCategory = createFinancialTransactionCategory(user2);

        FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO =
                new FinancialTransactionCategoryUpdateDTO(CATEGORY_NAME, FinancialTransactionType.EXPENSE);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.patch(ENDPOINT_CATEGORIES_PATCH, financialTransactionCategory.getId())
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryUpdateDTO))
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isNotFound());
    }

    private User createUser(String email) {
        final User userOne = User.builder()
                .userName("UserTest")
                .email(email)
                .password("Password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();

        return userRepository.save(userOne);
    }

    private FinancialTransactionCategory createFinancialTransactionCategory(User user) {
        final FinancialTransactionCategory financialTransactionCategory = FinancialTransactionCategory.builder()
                .name(CATEGORY_NAME + user.getId())
                .type(FinancialTransactionType.INCOME)
                .user(user)
                .build();

        return financialTransactionCategoryRepository.save(financialTransactionCategory);
    }

}
