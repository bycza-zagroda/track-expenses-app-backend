package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindFinancialTransactionCategoryIT extends BaseIntegrationTestIT {

    @Autowired
    FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    private String validAccessToken;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        financialTransactionCategoryRepository.deleteAll();

        User user = new User();
        user.setUserName("test");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setUserStatus(UserStatus.VERIFIED);
        userRepository.save(user);

        validAccessToken = userService.createAccessToken(user);
    }

    @DisplayName("When search category by id should return proper financial transaction category")
    @Test
    public void testGetFinancialTransactionCategory_whenProperId_shouldReturnFinancialTransactionCategory()
            throws Exception {
        User userTest = userRepository.findByEmail("test@example.com").get();
        FinancialTransactionCategory financialTransactionCategory = testFinancialTransactionCategory(userTest);

        mockMvc.perform(get("/api/categories/{id}", financialTransactionCategory.getId())
                        .header("Authorization", "Bearer " + validAccessToken)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.financialTransactionCategoryDTO.id")
                        .value(financialTransactionCategory.getId()))
                .andExpect(jsonPath("$.financialTransactionCategoryDTO.name")
                        .value("testName"));
        Assertions.assertEquals(1, financialTransactionCategoryRepository.count());
    }

    @DisplayName("Should return message financial transaction category not found and statusCode: 404")
    @Test
    public void testGetFinancialTransactionCategory_whenIdIsNotExists_shouldReturnErrorCodeAndStatus404()
            throws Exception {
        Long nonExistentCategoryId = 999L;

        mockMvc.perform(get("/api/categories/{id}", nonExistentCategoryId)
                        .header("Authorization", "Bearer " + validAccessToken)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @DisplayName("Should return list of financial transaction categories")
    @Test
    public void testGetFinancialTransactionCategories() throws Exception {
        User userTest = userRepository.findByEmail("test@example.com").get();
        FinancialTransactionCategory financialTransactionCategory = testFinancialTransactionCategory(userTest);
        FinancialTransactionCategory financialTransactionCategory1 = testFinancialTransactionCategory(userTest);
        FinancialTransactionCategory financialTransactionCategory2 = testFinancialTransactionCategory(userTest);

        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Bearer " + validAccessToken)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(financialTransactionCategory.getId()))
                .andExpect(jsonPath("$.[1].id").value(financialTransactionCategory1.getId()))
                .andExpect(jsonPath("$.[2].id").value(financialTransactionCategory2.getId()))
                .andExpect(jsonPath("$.[0].name").value(financialTransactionCategory.getName()))
                .andExpect(jsonPath("$.[1].name").value(financialTransactionCategory1.getName()))
                .andExpect(jsonPath("$.[2].name").value(financialTransactionCategory2.getName()));
        Assertions.assertEquals(3, financialTransactionCategoryRepository.count());
    }

    private FinancialTransactionCategory testFinancialTransactionCategory(User user) {
        final FinancialTransactionCategory testCategory = FinancialTransactionCategory.builder()
                .user(user)
                .creationDate(Instant.now())
                .type(FinancialTransactionType.INCOME)
                .name("testName")
                .financialTransactions(null)
                .build();
        return financialTransactionCategoryRepository.save(testCategory);
    }

}
