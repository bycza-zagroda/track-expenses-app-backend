package pl.byczazagroda.trackexpensesappbackend;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Access to protected endpoint should be forbidden without login")
    public void accessProtected_ShouldBeForbidden_WithoutLogin() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", password = "testPass", roles = "USER")
    @DisplayName("Access to protected endpoint should be allowed with login")
    public void accessProtected_ShouldBeAllowed_WithLogin() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Access to open endpoint should be allowed to everyone")
    public void accessOpen_ShouldBeAllowed_ToEveryone() throws Exception {
        mockMvc.perform(get("/api/testOpen"))
                .andExpect(status().isOk());
    }

}

