package pl.byczazagroda.trackexpensesappbackend.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    /**
     * ObjectMapper provides functionality for reading and writing JSON,
     * either to and from basic POJOs (Plain Old Java Objects), or to and
     * from a general-purpose JSON Tree Model (JsonNode), as well as related
     * functionality for performing conversions.
     */
    private final ObjectMapper objectMapper;

    /**
     * Customize ObjectMapper provides functionality for reading and writing
     * JSON.
     */
    public void customizeObjectMapper() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
    }
}
