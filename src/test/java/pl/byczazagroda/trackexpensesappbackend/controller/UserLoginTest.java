package pl.byczazagroda.trackexpensesappbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.byczazagroda.trackexpensesappbackend.config.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {UserServiceImpl.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
public class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final String LOGIN_URL = "/api/auth/login";

    private static final String TEST_PASSWORD = "Exampletestpassword123*";

    private static final String TEST_EMAIL = "test@gmail.com";

    private static final String TEST_USERNAME = "TestUser";

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        given(userRepository.findByEmail(TEST_EMAIL)).willReturn(
                Optional.of(new User(1L, List.of(), TEST_USERNAME,
                        TEST_EMAIL, hashPassword(TEST_PASSWORD), UserStatus.VERIFIED, List.of()))
        );

    }

    @DisplayName("When user credentials are valid and remember_me option is disabled, should return only access_token")
    @Test
    void testLoginUser_whenUserCredentialsAreOkAndIsRememberMeIsFalse_thenShouldReturnOnlyAccessToken() throws Exception {
        AuthLoginDTO loginDTO = new AuthLoginDTO(TEST_EMAIL, TEST_PASSWORD, false);
        mockMvc.perform(post(LOGIN_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().doesNotExist("refresh_token"));
    }

    @DisplayName("When user credentials are valid and remember_me option is enabled, should return access_token and refresh token")
    @Test
    void testLoginUser_whenUserCredentialsAreOkAndIsRememberMeIsTrue_thenShouldReturnOnlyAccessTokenAndRefreshToken() throws Exception {
        AuthLoginDTO loginDTO = new AuthLoginDTO(TEST_EMAIL, TEST_PASSWORD, true);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().exists("refresh_token"));
    }

    @DisplayName("When user credentials are invalid, should return an 401 error response")
    @Test
    void testLoginUser_whenUserCredentialsAreBad_thenShouldReturnErrorResponse() throws Exception {
        AuthLoginDTO loginDTO = new AuthLoginDTO(TEST_EMAIL, "wrongpasswordAAAAA123*", false);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("refresh_token"));
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
