package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteFinancialCategoryIT extends BaseIntegrationTestIT {

    @MockBean
    private FinancialTransactionCategoryRepository categoryRepository;

    @MockBean
    private UserRepository userRepository;

    private final Long testUserId = 1L;

    private final Long otherUserId = 2L;

    private final Long testCategoryId = 1L;

    private final Long nonExistentCategoryId = 999L;
    
    private final String deleteCategoryUrl = "/api/categories/{id}";

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setId(testUserId);
        testUser.setUserName("Test User");
        testUser.setPassword("password");

        User otherUser = new User();
        otherUser.setId(otherUserId);
        otherUser.setUserName("Other User");
        otherUser.setPassword("password");

        FinancialTransactionCategory testCategory = new FinancialTransactionCategory();
        testCategory.setId(testCategoryId);
        testCategory.setName("Test Category");
        testCategory.setUser(testUser);

        userRepository.save(testUser);
        userRepository.save(otherUser);
        categoryRepository.save(testCategory);
    }

    @Test
    @DisplayName("Should delete category when user is owner and category exists")
    void shouldDeleteCategoryWhenExists() throws Exception {
        mockMvc.perform(delete(deleteCategoryUrl, testCategoryId)
                        .principal(() -> String.valueOf(testUserId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(categoryRepository.existsById(testCategoryId)).isFalse();
    }

    @Test
    @DisplayName("Should not delete category when category ID is non-existent")
    void shouldNotDeleteCategoryWhenNotExists() throws Exception {
        mockMvc.perform(delete(deleteCategoryUrl, nonExistentCategoryId)
                        .principal(() -> String.valueOf(testUserId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not delete category when category belongs to another user")
    void shouldNotDeleteCategoryWhenBelongsToAnotherUser() throws Exception {
        mockMvc.perform(delete(deleteCategoryUrl, testCategoryId)
                        .principal(() -> String.valueOf(otherUserId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
