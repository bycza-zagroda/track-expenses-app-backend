package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRegistrationIT extends BaseIntegrationTestIT {

    private static final String REGISTER_USER_URL = "/api/auth/register";

    private static final AuthRegisterDTO REGISTER_DTO =
            new AuthRegisterDTO("user@server.com", "User123@", "User_Bolek");

    private static final AuthRegisterDTO REGISTER_DTO_TOO_SHORT_PASSWORD =
            new AuthRegisterDTO("user@server.com", "short", "User_Bolek");

    private static final AuthRegisterDTO REGISTER_DTO_INVALID_EMAIL =
            new AuthRegisterDTO("InvalidEmail", "User123@", "User_Bolek");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @DisplayName("When a new user is registered, it should save user and return CREATED status")
    @Test
    void testRegisterUser_whenNewUserIsAdded_thenShouldCreateUserAndReturn201Status() throws Exception {

        mockMvc.perform(post(REGISTER_USER_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(REGISTER_DTO)))
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail(REGISTER_DTO.email()).orElse(null);
        assertNotNull(user);
        assertEquals(REGISTER_DTO.email(), user.getEmail());
        assertEquals(REGISTER_DTO.username(), user.getUserName());
        assertNotEquals(REGISTER_DTO.password(), user.getPassword());
    }

    @DisplayName("When trying to register a user that already exists, it should return BAD_REQUEST")
    @Test
    void testRegisterUser_whenUserAlreadyExists_thenShouldReturnErrorResponse() throws Exception {
        final User existingUser = User.builder()
                .email("user@server.com")
                .userName("User_Bolek")
                .password("User123@")
                .userStatus(UserStatus.UNVERIFIED)
                .build();

        userRepository.save(existingUser);

        mockMvc.perform(post(REGISTER_USER_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(REGISTER_DTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("When registering a user with a password that's too short, it should return BAD_REQUEST")
    @Test
    void testRegisterUser_whenPasswordIsTooShort_thenShouldReturnErrorResponse() throws Exception {
        mockMvc.perform(post(REGISTER_USER_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(REGISTER_DTO_TOO_SHORT_PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("When registering a user with an invalid email, it should return BAD_REQUEST")
    @Test
    void testRegisterUser_whenEmailIsInvalid_thenShouldReturnErrorResponse() throws Exception {
        mockMvc.perform(post(REGISTER_USER_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(REGISTER_DTO_INVALID_EMAIL)))
                .andExpect(status().isBadRequest());
    }

}
