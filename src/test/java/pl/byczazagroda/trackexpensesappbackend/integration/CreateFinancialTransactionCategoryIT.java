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
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

class CreateFinancialTransactionCategoryIT extends BaseIntegrationTestIT {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void clearDatabase() {
        financialTransactionRepository.deleteAll();
        financialTransactionCategoryRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should return ResponseStatus IsCreated when create financial transaction category")
    @Test
    void testCreateFinancialTransactionCategory_whenValidDataProvided_thenShouldCreateCategory(
    ) throws Exception {
        var testUser = createTestUser();
        var financialTransactionCategoryCreateDTO
                = new FinancialTransactionCategoryCreateDTO("Category",
                FinancialTransactionType.INCOME,
                testUser.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(testUser.getId())))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Category"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("INCOME"));

        Assertions.assertEquals(1, financialTransactionCategoryRepository.count());
    }

    @DisplayName("Should return ResponseStatus BadRequest when name length is greater than 30")
    @Test
    void testCreateFinancialTransactionCategory_whenNameExceeds30Characters_thenShouldReturnBadRequest()
            throws Exception {
        var financialTransactionCategoryCreateDTO
                = new FinancialTransactionCategoryCreateDTO("ThisIsVeryLongNameForCategoryMoreThan30Characters",
                FinancialTransactionType.INCOME,
                1L);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user("1")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode()));

        Assertions.assertEquals(0, financialTransactionCategoryRepository.count());
        Assertions.assertTrue(result.andReturn().getResolvedException() instanceof MethodArgumentNotValidException);
        Assertions.assertEquals(ErrorCode.TEA003.getBusinessStatusCode(), result.andReturn().getResponse().getStatus());
    }

    @DisplayName("Should return ResponseStatus BadRequest when name is empty")
    @Test
    void testCreateFinancialTransactionCategory_whenNameIsEmpty_thenShouldReturnBadRequest() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO("",
                FinancialTransactionType.INCOME,
                1L);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user("1"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode()));

        Assertions.assertTrue(result.andReturn().getResolvedException() instanceof MethodArgumentNotValidException);
        Assertions.assertEquals(ErrorCode.TEA003.getBusinessStatusCode(), result.andReturn().getResponse().getStatus());
    }

    @DisplayName("Should return ResponseStatus BadRequest when name contains invalid characters")
    @Test
    void testCreateFinancialTransactionCategory_whenNameContainsInvalidCharacters_thenReturnShouldBadRequest(
    ) throws Exception {
        User testUser = createTestUser();
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO(
                "Catego*&*^ry@",
                FinancialTransactionType.INCOME,
                1L);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(testUser.getId()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode()));

        Assertions.assertTrue(result.andReturn().getResolvedException() instanceof MethodArgumentNotValidException);
        Assertions.assertEquals(ErrorCode.TEA003.getBusinessStatusCode(), result.andReturn().getResponse().getStatus());
    }

    @DisplayName("Should return ResponseStatus BadRequest when type is empty")
    @Test
    void testCreateFinancialTransactionCategory_whenTypeIsEmpty_thenShouldReturnBadRequest() throws Exception {
        User testUser = createTestUser();
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO(
                "Category",
                null,
                1L);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(testUser.getId()))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode()));

        Assertions.assertTrue(result.andReturn().getResolvedException() instanceof MethodArgumentNotValidException);
        Assertions.assertEquals(ErrorCode.TEA003.getBusinessStatusCode(), result.andReturn().getResponse().getStatus());
    }

    private User createTestUser() {
        final User userOne = User.builder()
                .id(1L)
                .userName("userone")
                .email("Email@wp.pl")
                .password("Password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
        return userRepository.save(userOne);
    }

}
