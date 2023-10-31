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


    @DisplayName("Should remove refresh token on user logout and return OK status")
    @Test
    void logout_ValidUser_ShouldRemoveRefreshTokenAndReturnOkStatus() throws Exception {

        User user = userRepository.save(TestUtils.createUserForTest());

        String validAccessToken = authService.createAccessToken(user);
        authService.createRefreshTokenCookie(user);

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("refresh_token"))
                .andExpect(cookie().maxAge("refresh_token", 0));
    }

    @DisplayName("Should return 'Unauthorized' status when a non-authenticated user tries to logout")
    @Test
    void logout_NonAuthenticatedUser_ShouldReturnUnauthorizedStatus() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isUnauthorized());

    }

}
