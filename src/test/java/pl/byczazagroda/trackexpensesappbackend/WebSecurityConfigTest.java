package pl.byczazagroda.trackexpensesappbackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.byczazagroda.trackexpensesappbackend.config.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.controller.UserController;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebSecurityConfig.class, ErrorStrategy.class}))
@ActiveProfiles("test")
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jwt.secret}")
    private String secret;

    private final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiYXV0aG9yaXRpZXMiOltdLCJpYXQiOjE1MTYyMzkwMjJ9.fY5zqz0LBU7dDyz5BCbdDzH0cMf_EJm-zlQICupScVw";

    private String createJwtToken () {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject("123")
                .withClaim("authorities", List.of())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(3600))
                .sign(algorithm);
    }

    @DisplayName("Check if authorized request with valid JWT returns status OK")
    @Test
    void testPerformAuthorizedRequestWithValidJWT_thenReturnStatusOk() throws Exception {
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization","Bearer " + createJwtToken()))
                .andExpect(status().isOk());
    }

    @DisplayName("Check if unauthorized request returns Unauthorized status")
    @Test
    void testPerformUnauthorizedRequest_thenReturnUnauthorizedStatus() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Check if authorized request with invalid JWT returns Unauthorized status")
    @Test
    void testPerformAuthorizedRequestWithInvalidJWT_thenReturnUnauthorizedStatus() throws Exception {
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + INVALID_TOKEN))
                .andExpect(status().isUnauthorized());
    }

}
