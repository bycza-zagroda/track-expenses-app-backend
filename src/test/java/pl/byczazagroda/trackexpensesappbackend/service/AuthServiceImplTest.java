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
import pl.byczazagroda.trackexpensesappbackend.auth.userModel.User;
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

    @DisplayName("A valid password should return a hash of the correct length")
    @Test
      void testHashPassword_whenPasswordIsValid_thenReturnHashed() {
        String password = "Password123!";
        String hashedPassword = userService.hashPassword(password);

      assertEquals(60, hashedPassword.length());
    }

    @DisplayName("When password is too short, an AppRuntimeException (U004) should be thrown")
    @Test
      void testHashPassword_whenPasswordIsTooShort_thenThrowException() {
        String shortPassword = "123";
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.hashPassword(shortPassword));

      assertEquals(ErrorCode.U004.getBusinessMessage(), exception.getMessage());
    }

    @DisplayName("When registering a new user, no exception should be thrown and the user should be saved")
    @Test
      void testRegisterUser_whenNewUser_thenSaveUser() {
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
      void testRegisterUser_whenEmailExists_thenThrowException() {
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
      void testRegisterUser_whenEmailIsInvalid_thenThrowException() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO_INVALID_EMAIL));

      assertEquals(ErrorCode.U002.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("When registering a user with too short password, an AppRuntimeException (U004) should be thrown")
    @Test
      void testRegisterUser_whenPasswordIsTooShort_thenThrowException() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO_TOO_SHORT_PASSWORD));

      assertEquals(ErrorCode.U004.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("When password is too long, an AppRuntimeException (U006) should be thrown")
    @Test
      void testHashPasswordWhenPasswordIsTooLongThenThrowException() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.hashPassword(tooLongPassword));

      assertEquals(ErrorCode.U007.getBusinessMessage(), exception.getMessage());
    }

    @DisplayName("When registering a user with too long password, an AppRuntimeException (U006) should be thrown")
    @Test
      void testRegisterUserWhenPasswordIsTooLongThenThrowException() {
        AppRuntimeException exception = assertThrows(AppRuntimeException.class,
                () -> userService.registerUser(REGISTER_DTO_TOO_LONG_PASSWORD));

      assertEquals(ErrorCode.U007.getBusinessMessage(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
