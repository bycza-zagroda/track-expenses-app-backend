package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceImplIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUser() {
        String url = "/api/auth/register";
        AuthRegisterDTO dto = new AuthRegisterDTO("test@test.com", "Test123!", "testuser");

        HttpEntity<AuthRegisterDTO> requestEntity = new HttpEntity<>(dto);
        ResponseEntity<Void> response = restTemplate
                .postForEntity(url, requestEntity, Void.class);

        assertEquals(HttpStatus.CREATED,
                response.getStatusCode());

        User user = userRepository.findByEmail(dto.email()).orElse(null);
        assertNotNull(user);
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.username(), user.getUserName());
        assertNotEquals(dto.password(), user.getPassword());
    }

    @Test
    public void testRegisterUserWithExistingEmail() {
        User existingUser = new User();
        existingUser.setEmail("test@test.com");
        existingUser.setUserName("existingUser");
        userRepository.save(existingUser);

        String url = "/api/auth/register";
        AuthRegisterDTO dto = new AuthRegisterDTO("test@test.com", "Test123!", "testuser");

        HttpEntity<AuthRegisterDTO> requestEntity = new HttpEntity<>(dto);
        ResponseEntity<String> response = restTemplate
                .postForEntity(url, requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        long count = userRepository.count();
        assertEquals(1, count);
    }

    @Test
    public void testRegisterUserWithShortPassword() {
        String url = "/api/auth/register";
        AuthRegisterDTO dto = new AuthRegisterDTO("test@test.com", "short", "testuser");

        HttpEntity<AuthRegisterDTO> requestEntity = new HttpEntity<>(dto);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST,
                response.getStatusCode());
    }

    @Test
    public void testRegisterUserWithInvalidEmail() {
        String url = "/api/auth/register";
        AuthRegisterDTO dto = new AuthRegisterDTO("invalid_email", "Test123!", "testuser");

        HttpEntity<AuthRegisterDTO> requestEntity = new HttpEntity<>(dto);
        ResponseEntity<String> response = restTemplate
                .postForEntity(url, requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST,
                response.getStatusCode());
    }

}
