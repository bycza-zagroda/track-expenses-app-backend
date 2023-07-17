package pl.byczazagroda.trackexpensesappbackend.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.byczazagroda.trackexpensesappbackend.config.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;
import pl.byczazagroda.trackexpensesappbackend.service.UserServiceImpl;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = AuthController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {UserServiceImpl.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
class RefreshTokenTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final String validAccessToken = "valid_access_token";

    private final String validRefreshToken = "valid_refresh_token";

    private final String newAccessToken = "new_access_token";

    private final String newRefreshToken = "new_refresh_token";

    private final String invalidRefreshToken = "invalid_refresh_token";

    private final String invalidAccessToken = "invalid_access_token";

    private final String expiredAccessToken = "expired_access_token";

    private final Cookie newRefreshTokenCookie = new Cookie("refresh_token", newRefreshToken);

    @DisplayName("Refresh tokens when both access and refresh tokens are valid")
    @Test
    @WithMockUser
    void refreshTokensWithCorrectTokens_thenReturnNewAccessTokenAndSetNewRefreshTokenInCookie() throws Exception {
        User user = new User();

        when(userService.validateToken(validAccessToken)).thenReturn(true);
        when(userService.validateToken(validRefreshToken)).thenReturn(true);
        when(userService.getUserFromToken(validAccessToken)).thenReturn(user);
        when(userService.createAccessToken(user)).thenReturn(newAccessToken);
        when(userService.createRefreshTokenCookie(user)).thenReturn(newRefreshTokenCookie);

        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", validRefreshToken))
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken))
                .andExpect(result -> {
                    String setCookieHeader = result.getResponse().getHeader("Set-Cookie");
                    assertTrue(setCookieHeader.contains("refresh_token=" + newRefreshToken));
                });
    }

    @DisplayName("Attempt to refresh tokens without refresh token")
    @Test
    void refreshTokenWithoutRefreshToken_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Attempt to refresh tokens with incorrect access token")
    @Test
    void refreshTokenWithIncorrectAccessToken_thenReturnUnauthorized() throws Exception {
        when(userService.validateToken(invalidAccessToken)).thenReturn(false);

        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", validRefreshToken))
                        .header("Authorization", "Bearer " + invalidAccessToken))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Attempt to refresh tokens with incorrect refresh token")
    @Test
    void refreshTokenWithIncorrectRefreshToken_thenReturnUnauthorized() throws Exception {
        when(userService.validateToken(validAccessToken)).thenReturn(true);
        when(userService.validateToken(invalidRefreshToken)).thenReturn(false);

        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", invalidRefreshToken))
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Attempt to refresh tokens with expired access token")
    @Test
    void refreshTokenWithExpiredAccessToken_thenReturnUnauthorized() throws Exception {
        User user = new User();

        when(userService.validateToken(expiredAccessToken)).thenReturn(false);
        when(userService.validateToken(validRefreshToken)).thenReturn(true);
        when(userService.getUserFromToken(expiredAccessToken)).thenThrow(new JWTVerificationException("Expired access token"));
        when(userService.createAccessToken(user)).thenReturn(newAccessToken);
        when(userService.createRefreshTokenCookie(user)).thenReturn(newRefreshTokenCookie);

        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(new Cookie("refresh_token", validRefreshToken))
                        .header("Authorization", "Bearer " + expiredAccessToken))
                .andExpect(status().isUnauthorized());
    }

}
