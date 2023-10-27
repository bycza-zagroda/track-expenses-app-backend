package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class FindFinancialTransactionCategoryIT extends BaseIntegrationTestIT {

    @Autowired
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    private AuthRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        financialTransactionCategoryRepository.deleteAll();
    }

    @DisplayName("Should retrieve the correct financial transaction category when provided with a valid ID")
    @Test
    void retrieveCategory_ValidIdGiven_ShouldReturnCorrespondingCategory()
            throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        FinancialTransactionCategory fTCategory = testFinancialTransactionCategory(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/{id}", fTCategory.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(user.getId()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.financialTransactionCategoryDTO.id")
                        .value(fTCategory.getId()))
                .andExpect(jsonPath("$.financialTransactionCategoryDTO.name")
                        .value("testName"));

        Assertions.assertEquals(1, financialTransactionCategoryRepository.count());
    }

    @DisplayName("Should return 'Not Found' status when trying to retrieve a financial transaction category with a non-existent ID")
    @Test
    void retrieveCategory_NonExistentIdGiven_ShouldReturnStatusNotFound()
            throws Exception {
        final Long nonExistentCategoryId = 999L;
        User user = userRepository.save(TestUtils.createUserForTest());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/{id}", nonExistentCategoryId)
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(user.getId()))))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @DisplayName("Should retrieve a list of all financial transaction categories")
    @Test
    void retrieveAllCategories_ShouldReturnListOfAllCategories() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        FinancialTransactionCategory ftc1 = testFinancialTransactionCategory(user);
        FinancialTransactionCategory ftc2 = testFinancialTransactionCategory(user);
        FinancialTransactionCategory ftc3 = testFinancialTransactionCategory(user);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories")
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(user.getId()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(ftc1.getId()))
                .andExpect(jsonPath("$.[1].id").value(ftc2.getId()))
                .andExpect(jsonPath("$.[2].id").value(ftc3.getId()))
                .andExpect(jsonPath("$.[0].name").value(ftc1.getName()))
                .andExpect(jsonPath("$.[1].name").value(ftc2.getName()))
                .andExpect(jsonPath("$.[2].name").value(ftc3.getName()));

        final long financialTransactionCategoryCount = 3L;
        Assertions.assertEquals(financialTransactionCategoryCount, financialTransactionCategoryRepository.count());
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
