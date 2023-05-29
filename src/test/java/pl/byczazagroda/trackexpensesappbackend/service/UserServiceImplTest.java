package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceImplTest {

    private static final AuthRegisterDTO REGISTER_DTO =
            new AuthRegisterDTO("test@test.com", "Test123@", "testuser");

    private static final AuthRegisterDTO REGISTER_DTO_TOO_SHORT_PASSWORD =
            new AuthRegisterDTO("test@test.com", "short", "testuser");

    private static final AuthRegisterDTO REGISTER_DTO_INVALID_EMAIL =
            new AuthRegisterDTO("InvalidEmail", "Test123@", "testuser");

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(userRepository);
    }

    @DisplayName("When password is valid, it should return a hashed password of correct length")
    @Test
    public void testHashPassword_whenPasswordIsValid_thenReturnHashed() {
        String password = "Password123!";
        String hashedPassword = userService.hashPassword(password);
        assertEquals(64, hashedPassword.length());
    }

    @DisplayName("When password is too short, an AppRuntimeException (U004) should be thrown")
    @Test
    public void testHashPassword_whenPasswordIsTooShort_thenThrowException() {
        String shortPassword = "123";
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.hashPassword(shortPassword));
        assertEquals(ErrorCode.U004.getBusinessMessage(), exception.getMessage());
    }

    @DisplayName("When registering a new user, no exception should be thrown and the user should be saved")
    @Test
    public void testRegisterUser_whenNewUser_thenSaveUser() {
        when(userRepository
                .existsByEmail(anyString()))
                .thenReturn(false);

        assertDoesNotThrow(() -> userService.registerUser(REGISTER_DTO));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User user = userCaptor.getValue();
        assertEquals(REGISTER_DTO.email(), user.getEmail());
        assertEquals(REGISTER_DTO.username(), user.getUserName());
        assertNotEquals(REGISTER_DTO.password(), user.getPassword());
    }

    @DisplayName("When registering a user with existing email, an AppRuntimeException (U001) should be thrown")
    @Test
    public void testRegisterUser_whenEmailExists_thenThrowException() {
        when(userRepository
                .existsByEmail(REGISTER_DTO.email()))
                .thenReturn(true);

        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO));
        assertEquals(ErrorCode.U001.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("When registering a user with invalid email, an AppRuntimeException (U002) should be thrown")
    @Test
    public void testRegisterUser_whenEmailIsInvalid_thenThrowException() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO_INVALID_EMAIL));
        assertEquals(ErrorCode.U002.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("When registering a user with too short password, an AppRuntimeException (U004) should be thrown")
    @Test
    public void testRegisterUser_whenPasswordIsTooShort_thenThrowException() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO_TOO_SHORT_PASSWORD));
        assertEquals(ErrorCode.U004.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}