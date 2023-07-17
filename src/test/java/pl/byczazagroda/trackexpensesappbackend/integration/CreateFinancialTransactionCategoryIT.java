package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionCategoryRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
//        financialTransactionRepository.deleteAll();
//        financialTransactionCategoryRepository.deleteAll();
//        walletRepository.deleteAll();
//        userRepository.deleteAll();

        User user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setPassword("password");
        user.setUserName("username");
        user.setUserStatus(UserStatus.VERIFIED);
        userRepository.save(user);
    }

    /*
    @PostMapping()
    public ResponseEntity<FinancialTransactionCategoryDTO> createFinancialTransactionCategory(
            @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO,
            Principal principal) {

        Long userId = Long.valueOf(principal.getName());

        FinancialTransactionCategoryDTO financialTransactionCategoryDTO =
                financialTransactionCategoryService.createFinancialTransactionCategory(financialTransactionCategoryCreateDTO, userId);

        return new ResponseEntity<>(financialTransactionCategoryDTO, HttpStatus.CREATED);
    }
    */

    @DisplayName("Should successfully create financial transaction category")
    @Test
    void testCreate() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO
                ("Category",
                FinancialTransactionType.INCOME,
                        1L);


        mockMvc.perform(post("/api/categories")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(user("1")))
//                        .principal(principal)
//                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Category"))
                .andExpect(jsonPath("$.type").value("INCOME"));

        assertEquals(1, financialTransactionCategoryRepository.count());
        assertEquals(0, walletRepository.count());
        assertEquals(1, userRepository.count());


    }

    @DisplayName("Should return error when name length is greater than 30")
    @Test
    void testCreateNameLengthGreaterThan30() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO
                ("ThisIsVeryLongNameForCategoryMoreThan30Characters",
                        FinancialTransactionType.INCOME,
                        1L);

        mockMvc.perform(post("/api/categories")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(user("1")))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));

        assertEquals(0, financialTransactionCategoryRepository.count());
        assertEquals(0, walletRepository.count());
        assertEquals(1, userRepository.count());
    }


    @DisplayName("Should return error when name is empty")
    @Test
    void testCreateNameEmpty() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO
                ("",
                        FinancialTransactionType.INCOME,
                        1L);

        mockMvc.perform(post("/api/categories")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(user("1"))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));

        assertEquals(0, financialTransactionCategoryRepository.count());
        assertEquals(0, walletRepository.count());
        assertEquals(1, userRepository.count());
    }

    @DisplayName("Should return error when name contains invalid characters")
    @Test
    void testCreateNameContainsInvalidCharacters() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO
                ("Catego*&*^ry@",
                        FinancialTransactionType.INCOME,
                        1L);

        mockMvc.perform(post("/api/categories")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(user("1"))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));

        assertEquals(0, financialTransactionCategoryRepository.count());
        assertEquals(0, walletRepository.count());
        assertEquals(1, userRepository.count());
    }

    //invalid type

    @DisplayName("Should return error when type is invalid")
    @Test
    void testCreateInvalidType() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO
                ("Category",
                        FinancialTransactionType.valueOf("INVALID"),
                        1L);

        mockMvc.perform(post("/api/categories")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(user("1"))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.TEA003.getBusinessStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.TEA003.getBusinessMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.TEA003.getBusinessStatusCode()));

        assertEquals(0, financialTransactionCategoryRepository.count());
        assertEquals(0, walletRepository.count());
        assertEquals(1, userRepository.count());
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


    private FinancialTransactionCategory createTestFinancialTransactionCategory() {
        final FinancialTransactionCategory testFinancialTransactionCategory = FinancialTransactionCategory.builder()
                .name("name")
                .type(FinancialTransactionType.INCOME)
                .user(createTestUser())
                .build();
        return financialTransactionCategoryRepository.save(testFinancialTransactionCategory);
    }

        private FinancialTransactionCategoryDTO createTestFinancialTransactionCategoryDTO (){
        return new FinancialTransactionCategoryDTO(1L, "name", FinancialTransactionType.INCOME, 1L);

    }
}
