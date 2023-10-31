package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteFinancialCategoryIT extends BaseIntegrationTestIT {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final String deleteCategoryUrl = "/api/categories/{id}";
    @Autowired
    private FinancialTransactionCategoryRepository categoryRepository;
    @Autowired
    private AuthRepository userRepository;
    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("Should successfully delete the category when the user is its owner and the category exists")
    void deleteCategory_UserIsOwnerAndCategoryExists_ShouldDeleteSuccessfully() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String token = authService.createAccessToken(user);

        FinancialTransactionCategory testCategory = createFinancialTransactionCategory(user);
        Long testCategoryId = testCategory.getId();

        mockMvc.perform(delete(deleteCategoryUrl, testCategoryId)
                        .header(AUTHORIZATION, BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(categoryRepository.existsById(testCategoryId)).isFalse();
    }

    @Test
    @DisplayName("Should not delete the category when the provided category ID does not exist")
    void deleteCategory_NonExistentCategoryIdGiven_ShouldReturnStatusNotFound() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String token = authService.createAccessToken(user);

        final Long nonExistentCategoryId = 999L;
        mockMvc.perform(delete(deleteCategoryUrl, nonExistentCategoryId)
                        .header(AUTHORIZATION, BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not delete the category when it belongs to a different user")
    void deleteCategory_CategoryBelongsToAnotherUser_ShouldReturnStatusNotFound() throws Exception {
        User user1 = userRepository.save(TestUtils.createUserForTest());

        User user2 = userRepository.save(TestUtils.createUserForTest());

        String token = authService.createAccessToken(user1);
        FinancialTransactionCategory otherCategory = createFinancialTransactionCategory(user2);
        Long otherCategoryId = otherCategory.getId();

        mockMvc.perform(delete(deleteCategoryUrl, otherCategoryId)
                        .header(AUTHORIZATION, BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        assertThat(categoryRepository.existsById(otherCategoryId)).isTrue();
    }

    private FinancialTransactionCategory createFinancialTransactionCategory(User user) {
        final FinancialTransactionCategory category = FinancialTransactionCategory.builder()
                .name("Test_Name")
                .user(user)
                .type(FinancialTransactionType.INCOME)
                .build();

        return categoryRepository.save(category);
    }

}
