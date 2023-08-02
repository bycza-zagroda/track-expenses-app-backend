package pl.byczazagroda.trackexpensesappbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserLogoutIT extends BaseIntegrationTestIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private String validAccessToken;

    @BeforeEach
    public void setup() {
        User user = new User();
        user.setUserName("test");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setUserStatus(UserStatus.VERIFIED);
        userRepository.save(user);

        validAccessToken = userService.createAccessToken(user);
        userService.createRefreshTokenCookie(user);
    }

    @DisplayName("When request user logout, should return 200 OK and remove refresh_token cookie")
    @Test
    void testRemoveRefreshToken_whenUserLogout_thenShouldReturnOkAndRemoveRefreshTokenFromCookie() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + validAccessToken))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("refresh_token"))
                .andExpect(cookie().maxAge("refresh_token", 0));
    }

    @DisplayName("When request user logout without earlier login, should return 401 Unauthorized status")
    @Test
    public void testLogout_shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isUnauthorized());

    }

}
