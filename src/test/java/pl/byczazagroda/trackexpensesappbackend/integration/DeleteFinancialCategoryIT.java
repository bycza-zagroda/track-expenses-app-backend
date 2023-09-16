package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.model.User;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

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
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Should delete category when user is owner and category exists")
    void shouldDeleteCategoryWhenExists() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String token = userService.createAccessToken(user);

        FinancialTransactionCategory testCategory = createFinancialTransactionCategory(user);
        Long testCategoryId = testCategory.getId();

        mockMvc.perform(delete(deleteCategoryUrl, testCategoryId)
                        .header(AUTHORIZATION, BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(categoryRepository.existsById(testCategoryId)).isFalse();
    }

    @Test
    @DisplayName("Should not delete category when category ID is non-existent")
    void shouldNotDeleteCategoryWhenNotExists() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        String token = userService.createAccessToken(user);

        final Long nonExistentCategoryId = 999L;
        mockMvc.perform(delete(deleteCategoryUrl, nonExistentCategoryId)
                        .header(AUTHORIZATION, BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not delete category when category belongs to another user")
    void shouldNotDeleteCategoryWhenBelongsToAnotherUser() throws Exception {
        User user1 = userRepository.save(TestUtils.createUserForTest());

        User user2 = userRepository.save(TestUtils.createUserForTest());

        String token = userService.createAccessToken(user1);
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
