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
import pl.byczazagroda.trackexpensesappbackend.auth.api.dto.AuthAccessTokenDTO;
import pl.byczazagroda.trackexpensesappbackend.auth.api.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.auth.userModel.User;
import pl.byczazagroda.trackexpensesappbackend.auth.userModel.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.impl.AuthServiceImpl;

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

    @DisplayName("When user credentials are valid and remember_me option is disabled, should return only access_token")
    @Test
    void testLoginUser_whenUserCredentialsAreOkAndIsRememberMeIsFalse_thenShouldReturnOnlyAccessToken()
            throws Exception {
        AuthLoginDTO loginDTO = new AuthLoginDTO(TEST_EMAIL, TEST_PASSWORD, false);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().doesNotExist("refresh_token"));
    }

    @DisplayName("When user credentials are valid and remember_me option is enabled, should return access_token " +
            "and refresh token")
    @Test
    void testLoginUser_whenUserCredentialsAreOkAndIsRememberMeIsTrue_thenShouldReturnOnlyAccessTokenAndRefreshToken()
            throws Exception {
        AuthLoginDTO loginDTO = new AuthLoginDTO(TEST_EMAIL, TEST_PASSWORD, true);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().exists("refresh_token"));
    }

    @DisplayName("When user credentials are invalid, should return an 400 error response")
    @Test
    void testLoginUser_whenUserCredentialsAreBad_thenShouldReturnBadRequestStatus() throws Exception {
        AuthLoginDTO loginDTO = new AuthLoginDTO(TEST_EMAIL, "wrongpasswordAAAAA123/)>", false);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(cookie().doesNotExist("refresh_token"));
    }

    @DisplayName("When user credentials are invalid, should return an 401 error response")
    @Test
    void testLoginUser_whenUserIsNotSignUp_thenShouldReturnUnauthorizedStatus() throws Exception {
        AuthLoginDTO loginDTO =
                new AuthLoginDTO("email@emila.com", "passwordAAAAA123@", false);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(cookie().doesNotExist("refresh_token"));
    }

    @DisplayName("Should return valid token when user credential are valid")
    @Test
    void testShouldReturnValidToken_WhenUserCredentialsAreValid() throws Exception {
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

    @DisplayName("Should return not valid and throw exception when user credentials are ok and created token is modified")
    @Test
    void testShouldThrowExceptionToken_WhenUserCredentialsAreOk_AndCreatedTokenIsModified() throws Exception {
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
                ()-> decodedJWT
                .verify(invalidAccessToken));
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
