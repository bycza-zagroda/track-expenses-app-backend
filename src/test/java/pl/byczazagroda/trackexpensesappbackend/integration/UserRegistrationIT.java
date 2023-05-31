package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRegistrationIT extends BaseIntegrationTestIT {

    private static final String REGISTER_USER_URL = "/api/auth/register";

    private static final AuthRegisterDTO REGISTER_DTO =
            new AuthRegisterDTO("user@server.com", "User123@", "User_Bolek");

    private static final AuthRegisterDTO REGISTER_DTO_TOO_SHORT_PASSWORD =
            new AuthRegisterDTO("user@server.com", "short", "User_Bolek");

    private static final AuthRegisterDTO REGISTER_DTO_INVALID_EMAIL =
            new AuthRegisterDTO("InvalidEmail", "User123@", "User_Bolek");

    final User existingUser = User.builder()
            .email("user@server.com")
            .userName("User Bolek")
            .password("Password123!")
            .userStatus(UserStatus.UNVERIFIED)
            .build();

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @DisplayName("When a new user is registered, it should create the user and return CREATED status")
    @Test
    void testRegisterUser_whenNewUser_thenShouldCreateUser() {
        ResponseEntity<User> response = restTemplate
                .postForEntity(REGISTER_USER_URL, REGISTER_DTO, User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User user = userRepository.findByEmail(REGISTER_DTO.email()).orElse(null);
        assertNotNull(user);
        assertEquals(REGISTER_DTO.email(), user.getEmail());
        assertEquals(REGISTER_DTO.username(), user.getUserName());
        assertNotEquals(REGISTER_DTO.password(), user.getPassword());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("When trying to register a user that already exists, it should return BAD_REQUEST")
    @Test
    void testRegisterUser_whenUserAlreadyExists_thenShouldReturnErrorResponse() {
        ResponseEntity<String> response = restTemplate
                .postForEntity(REGISTER_USER_URL, REGISTER_DTO, String.class);

        final User existingUser = User.builder()
                .email("user@server.com")
                .userName("User Bolek")
                .password("Password123!")
                .userStatus(UserStatus.UNVERIFIED)
                .build();

        userRepository.save(existingUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, userRepository.count());
    }

    @DisplayName("When registering a user with a password that's too short, it should return BAD_REQUEST")
    @Test
    void testRegisterUser_whenPasswordIsTooShort_thenShouldReturnErrorResponse() {
        ResponseEntity<String> response = restTemplate
                .postForEntity(REGISTER_USER_URL, REGISTER_DTO_TOO_SHORT_PASSWORD, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @DisplayName("When registering a user with an invalid email, it should return BAD_REQUEST")
    @Test
    void testRegisterUser_whenEmailIsInvalid_thenShouldReturnErrorResponse() {
        ResponseEntity<String> response = restTemplate
                .postForEntity(REGISTER_USER_URL, REGISTER_DTO_INVALID_EMAIL, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
