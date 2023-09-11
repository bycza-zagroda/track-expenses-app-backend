package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.IntegrationTestUtils;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteFinancialCategoryIT extends BaseIntegrationTestIT {

    @Autowired
    private FinancialTransactionCategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static final String AUTHORIZATION = "Authorization";

    private static final String BEARER = "Bearer ";

    private final Long nonExistentCategoryId = 999L;

    private final String deleteCategoryUrl = "/api/categories/{id}";

    @Test
    @DisplayName("Should delete category when user is owner and category exists")
    void shouldDeleteCategoryWhenExists() throws Exception {
        User testUser = IntegrationTestUtils.createTestUser(userRepository);
        String token = userService.createAccessToken(testUser);
        FinancialTransactionCategory testCategory = createFinancialTransactionCategory(testUser);
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
        User testUser = IntegrationTestUtils.createTestUser(userRepository);
        String token = userService.createAccessToken(testUser);

        mockMvc.perform(delete(deleteCategoryUrl, nonExistentCategoryId)
                        .header(AUTHORIZATION, BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not delete category when category belongs to another user")
    void shouldNotDeleteCategoryWhenBelongsToAnotherUser() throws Exception {
        User testUser = IntegrationTestUtils.createTestUser(userRepository);
        User otherUser = IntegrationTestUtils.createTestUser(userRepository);
        String token = userService.createAccessToken(testUser);
        FinancialTransactionCategory otherCategory = createFinancialTransactionCategory(otherUser);
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
