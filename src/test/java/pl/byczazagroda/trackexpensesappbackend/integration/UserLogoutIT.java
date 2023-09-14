package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserLogoutIT extends BaseIntegrationTestIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @DisplayName("When request user logout, should return 200 OK and remove refresh_token cookie")
    @Test
    void testRemoveRefreshToken_whenUserLogout_thenShouldReturnOkAndRemoveRefreshTokenFromCookie() throws Exception {

        User user = userRepository.save(TestUtils.createTestUser());

        String validAccessToken = userService.createAccessToken(user);
        userService.createRefreshTokenCookie(user);

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
