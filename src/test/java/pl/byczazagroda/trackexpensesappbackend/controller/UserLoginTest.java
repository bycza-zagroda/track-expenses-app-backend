package pl.byczazagroda.trackexpensesappbackend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.byczazagroda.trackexpensesappbackend.auth.AuthController;
import pl.byczazagroda.trackexpensesappbackend.auth.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.dto.AuthAccessTokenDTO;
import pl.byczazagroda.trackexpensesappbackend.auth.api.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.auth.impl.AuthServiceImpl;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorStrategy;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {AuthServiceImpl.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final String LOGIN_URL = "/api/auth/login";

    private static final String TEST_PASSWORD = "Exampletestpassword123*@";

    private static final String TEST_EMAIL = "test@gmail.com";

    private static final String TEST_USERNAME = "TestUser";

    @MockBean
    private AuthRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    @BeforeEach
    void setup() {
        given(userRepository.findByEmail(TEST_EMAIL)).willReturn(
                Optional.of(new User(1L, List.of(), TEST_USERNAME,
                        TEST_EMAIL, hashPassword(TEST_PASSWORD), UserStatus.VERIFIED, List.of()))
        );

    }

    @DisplayName("Should return only access token for valid credentials without remember_me")
    @Test
    void loginUser_ValidCredentialsAndNoRememberMe_ShouldReturnAccessToken()
            throws Exception {
        AuthLoginDTO loginDTO = new AuthLoginDTO(TEST_EMAIL, TEST_PASSWORD, false);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().doesNotExist("refresh_token"));
    }

    @DisplayName("Should return access and refresh tokens for valid credentials with remember_me")
    @Test
    void loginUser_ValidCredentialsAndRememberMe_ShouldReturnAccessTokenAndRefreshToken()
            throws Exception {
        AuthLoginDTO loginDTO = new AuthLoginDTO(TEST_EMAIL, TEST_PASSWORD, true);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().exists("refresh_token"));
    }

    @DisplayName("Should return 400 error for invalid credentials")
    @Test
    void loginUser_InvalidCredentials_ShouldReturnBadRequestStatus() throws Exception {
        AuthLoginDTO loginDTO = new AuthLoginDTO(TEST_EMAIL, "wrongpasswordAAAAA123/)>", false);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist("refresh_token"));
    }

    @DisplayName("Should return 401 error when user is not signed up")
    @Test
    void loginUser_UserNotRegistered_ShouldReturnStatusUnauthorized() throws Exception {
        AuthLoginDTO loginDTO =
                new AuthLoginDTO("email@emila.com", "passwordAAAAA123@", false);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("refresh_token"));
    }

    @DisplayName("Should return valid token for valid user credentials")
    @Test
    void generateToken_ValidUserCredentials_ShouldReturnValidToken() throws Exception {
        AuthLoginDTO loginDTO =
                new AuthLoginDTO(TEST_EMAIL, TEST_PASSWORD, false);
        MvcResult result = mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(cookie().doesNotExist("refresh_token"))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        final AuthAccessTokenDTO authAccessTokenDTO = objectMapper.readValue(content, AuthAccessTokenDTO.class);

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(authAccessTokenDTO.accessToken());

        assertThat(decodedJWT.getSubject()).isNotBlank();
        assertThat(TEST_EMAIL).isEqualTo(decodedJWT.getClaim("email").asString());
    }

    @DisplayName("Should throw an exception for a modified token with valid credentials")
    @Test
    void verifyToken_ValidCredentialsButModifiedToken_ShouldThrowException() throws Exception {
        AuthLoginDTO loginDTO =
                new AuthLoginDTO(TEST_EMAIL, TEST_PASSWORD, false);
        MvcResult result = mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(cookie().doesNotExist("refresh_token"))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        final AuthAccessTokenDTO authAccessTokenDTO = objectMapper.readValue(content, AuthAccessTokenDTO.class);

        JWTVerifier decodedJWT = JWT.require(Algorithm.HMAC256(secret))
                .build();
        final String invalidAccessToken = authAccessTokenDTO.accessToken() + "bbb";

        assertThrows(SignatureVerificationException.class,
                () -> decodedJWT
                .verify(invalidAccessToken));
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
