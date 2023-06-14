package pl.byczazagroda.trackexpensesappbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseIntegrationTestIT {

    static final MySQLContainer<?> MY_SQL_CONTAINER;
    static final int TESTCONTAINER_STARTUP_TIMEOUT = 480;

    static {
        MY_SQL_CONTAINER =
                new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                        .withDatabaseName("trackexpensesapptest")
                        .withUsername("root")
                        .withPassword("root")
                        .withStartupTimeoutSeconds(TESTCONTAINER_STARTUP_TIMEOUT)
                        .withCommand("--innodb-redo-log-capacity=32M")
                        .withUrlParam("serverTimezone", "UTC");
        MY_SQL_CONTAINER.start();
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;


    @DynamicPropertySource
    private static void containerConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", MY_SQL_CONTAINER::getDriverClassName);
        registry.add("spring.profiles.active", () -> "test");
    }
}
