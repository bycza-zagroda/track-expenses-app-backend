package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RefreshTokenIT extends BaseIntegrationTestIT {

    private final String invalidAccessToken = "invalid_access_token";

    private final String invalidRefreshToken = "invalid_refresh_token";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private String validAccessToken;

    private String validRefreshToken;


    @BeforeEach
    public void setup() {
        User user = userRepository.save(TestUtils.createUserForTest());

        validAccessToken = userService.createAccessToken(user);
        Cookie refreshTokenCookie = userService.createRefreshTokenCookie(user);
        validRefreshToken = refreshTokenCookie.getValue();
    }

    @DisplayName("When request contains valid access_token and refresh_token, should return 200 OK and update refresh_token cookie")
    @Test
    void testRefreshToken_whenAccessTokenAndRefreshTokenValid_thenShouldReturnOkAndUpdateRefreshToken() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", validRefreshToken))
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("refresh_token"));
    }

    @DisplayName("When request does not contain refresh_token cookie, should return 500 isInternalServerError")
    @Test
    void testRefreshToken_whenNoRefreshTokenCookie_thenShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("When request contains invalid access_token, should return 403 Forbidden")
    @Test
    void testRefreshToken_whenAccessTokenInvalid_thenShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", validRefreshToken))
                        .header("Authorization", "Bearer " + invalidAccessToken))
                .andExpect(status().isForbidden());
    }

    @DisplayName("When request contains invalid refresh_token cookie, should return 403 Forbidden")
    @Test
    void testRefreshToken_whenRefreshTokenInvalid_thenShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", invalidRefreshToken))
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isForbidden());
    }

}
