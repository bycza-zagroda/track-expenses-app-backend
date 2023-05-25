package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import org.mockito.ArgumentCaptor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Test
    public void testValidateEmail() {
        String validEmail = "test@example.com";
        assertDoesNotThrow(() -> userService.validateEmail(validEmail));
    }
    @Test
    public void testValidateEmailWithInvalidEmail() {
        String invalidEmail = "test";
        assertThrows(IllegalArgumentException.class, () -> userService.validateEmail(invalidEmail));
    }
    @Test
    public void testHashPassword() {
        String password = "Password123!";
        String hashedPassword = userService.hashPassword(password);
        assertTrue(hashedPassword.length() > 0);
    }
    @Test
    public void testHashPasswordWithShortPassword() {
        String shortPassword = "123";
        assertThrows(IllegalArgumentException.class, () -> userService.hashPassword(shortPassword));
    }

    @Test
    public void testRegisterUser() {
        AuthRegisterDTO dto = new AuthRegisterDTO("test@test.com", "Test123!", "testuser");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        assertDoesNotThrow(() -> userService.registerUser(dto));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User user = userCaptor.getValue();
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.username(), user.getUserName());
        assertNotEquals(dto.password(), user.getPassword());
    }

    @Test
    public void testRegisterUserWithExistingEmail() {
        AuthRegisterDTO dto = new AuthRegisterDTO("test@test.com", "Test123!", "testuser");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUserWithInvalidEmail() {
        AuthRegisterDTO dto = new AuthRegisterDTO("invalid_email", "Test123!", "testuser");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUserWithShortPassword() {
        AuthRegisterDTO dto = new AuthRegisterDTO("test@test.com", "short", "testuser");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(dto));
        verify(userRepository, never()).save(any(User.class));
    }
}
