package pl.byczazagroda.trackexpensesappbackend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import pl.byczazagroda.trackexpensesappbackend.BaseControllerTestIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {
        "classpath:db/insert_into_wallets.sql"
})
class HelloWorldControllerTestIT extends BaseControllerTestIT {

    @Test
    @DisplayName("Should say 'Hello World!'")
    void shouldSayHelloWorld() throws Exception {
        // given

        // when
        String result = mockMvc.perform(get("/api/hello")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        assertEquals("Hello World!", result);
    }
}