package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.byczazagroda.trackexpensesappbackend.auth.api.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.auth.impl.AuthServiceImpl;
import pl.byczazagroda.trackexpensesappbackend.general.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
  class AuthServiceImplTest {

    private static String tooLongPassword = "@ehGtbD2fFH$2%P*P8WDUG3R&jOa4xvr#nK81*%m4#&1nATIu1@ehGtbD2fFH$2%"
            + " P*P8WDUG3R&jOa4xvr#nK81*%m4#&1nATIu1@ehGtbD2fFH$2%P*P8WDUG3R";

    private static final AuthRegisterDTO REGISTER_DTO =
            new AuthRegisterDTO("user@server.com", "User123@", "User_Bolek");

    private static final AuthRegisterDTO REGISTER_DTO_TOO_SHORT_PASSWORD =
            new AuthRegisterDTO("user@server.com", "short", "User_Bolek");

    private static final AuthRegisterDTO REGISTER_DTO_INVALID_EMAIL =
            new AuthRegisterDTO("InvalidEmail", "User123@", "User_Bolek");

    private static final AuthRegisterDTO REGISTER_DTO_TOO_LONG_PASSWORD =
            new AuthRegisterDTO("user@server.com",
                    tooLongPassword, "User_Test");

    @Spy
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private AuthRepository userRepository;

    @InjectMocks
    private AuthServiceImpl userService;

    @BeforeEach
      void setUp() {
        Mockito.reset(userRepository);
    }

    @DisplayName("Hashing a valid password should produce a result of correct length")
    @Test
      void hash_ValidPassword_ShouldReturnCorrectLengthHash() {
        String password = "Password123!";
        String hashedPassword = userService.hashPassword(password);

      assertEquals(60, hashedPassword.length());
    }

    @DisplayName("Hashing a too short password should throw U004 AppRuntimeException")
    @Test
      void hash_TooShortPassword_ShouldThrowU004Exception() {
        String shortPassword = "123";
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.hashPassword(shortPassword));

      assertEquals(ErrorCode.U004.getBusinessMessage(), exception.getMessage());
    }

    @DisplayName("Registering a new user should not throw any exceptions and the user should be saved")
    @Test
      void register_NewUser_ShouldSaveWithoutExceptions() {
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

    @DisplayName("Registering a user with an existing email should throw U001 AppRuntimeException")
    @Test
      void register_ExistingEmail_ShouldThrowU001Exception() {
        when(userRepository
                .existsByEmail(REGISTER_DTO.email()))
                .thenReturn(true);

        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO));
        assertEquals(ErrorCode.U001.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Registering a user with an invalid email should throw U002 AppRuntimeException")
    @Test
      void register_InvalidEmail_ShouldThrowU002Exception() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO_INVALID_EMAIL));

      assertEquals(ErrorCode.U002.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Registering a user with a too short password should throw U004 AppRuntimeException")
    @Test
      void register_TooShortPassword_ShouldThrowU004Exception() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO_TOO_SHORT_PASSWORD));

      assertEquals(ErrorCode.U004.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Hashing a too long password should throw U007 AppRuntimeException")
    @Test
      void hash_TooLongPassword_ShouldThrowU007Exception() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.hashPassword(tooLongPassword));

      assertEquals(ErrorCode.U007.getBusinessMessage(), exception.getMessage());
    }

    @DisplayName("Registering a user with a too long password should throw U007 AppRuntimeException")
    @Test
      void register_TooLongPassword_ShouldThrowU007Exception() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO_TOO_LONG_PASSWORD));

      assertEquals(ErrorCode.U007.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
