package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthService;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RefreshTokenIT extends BaseIntegrationTestIT {

    private final String invalidAccessToken = "invalid_access_token";

    private final String invalidRefreshToken = "invalid_refresh_token";

    @Autowired
    private AuthRepository userRepository;

    @Autowired
    private AuthService authService;

    private String validAccessToken;

    private String validRefreshToken;


    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        User user = userRepository.save(TestUtils.createUserForTest());

        validAccessToken = authService.createAccessToken(user);
        Cookie refreshTokenCookie = authService.createRefreshTokenCookie(user);
        validRefreshToken = refreshTokenCookie.getValue();
    }

    @DisplayName("Should refresh tokens when both access and refresh tokens are valid")
    @Test
    void refreshTokens_ValidTokensGiven_ShouldUpdateRefreshTokenCookie() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", validRefreshToken))
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("refresh_token"));
    }

    @DisplayName("Should return 'Internal Server Error' status when refresh token is missing")
    @Test
    void refreshTokens_NoRefreshTokenGiven_ShouldReturnStatusInternalServerError() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("Should return 'Forbidden' status when access token is invalid")
    @Test
    void refreshTokens_InvalidAccessTokenGiven_ShouldReturnStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", validRefreshToken))
                        .header("Authorization", "Bearer " + invalidAccessToken))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Should return 'Forbidden' status when refresh token is invalid")
    @Test
    void refreshTokens_InvalidRefreshTokenGiven_ShouldReturnStatusForbidden() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", invalidRefreshToken))
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isForbidden());
    }

}
