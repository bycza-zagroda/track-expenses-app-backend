package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserLogoutIT extends BaseIntegrationTestIT {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthRepository userRepository;


    @DisplayName("When request user logout, should return 200 OK and remove refresh_token cookie")
    @Test
    void testRemoveRefreshToken_whenUserLogout_thenShouldReturnOkAndRemoveRefreshTokenFromCookie() throws Exception {

        User user = userRepository.save(TestUtils.createUserForTest());

        String validAccessToken = authService.createAccessToken(user);
        authService.createRefreshTokenCookie(user);

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("refresh_token"))
                .andExpect(cookie().maxAge("refresh_token", 0));
    }

    @DisplayName("When request user logout without earlier login, should return 401 Unauthorized status")
    @Test
    void testLogout_shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isUnauthorized());

    }

}
