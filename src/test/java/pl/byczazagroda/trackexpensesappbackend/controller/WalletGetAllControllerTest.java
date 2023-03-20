package pl.byczazagroda.trackexpensesappbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.service.WalletServiceImpl;

import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WalletModelMapper.class, ErrorStrategy.class}))
@ActiveProfiles("test")
class WalletGetAllControllerTest {

    private static final String NAME_1 = "wallet name one";
    private static final String NAME_2 = "wallet name two";
    private static final String NAME_3 = "wallet name three";
    private static final String NAME_4 = "wallet name four";
    private static final String NAME_5 = "wallet name five";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get all five wallets")
    @BeforeEach
    void getAllWallets() throws Exception {
        //given
        List<WalletCreateDTO> expectedWallets = List.of(new WalletCreateDTO(NAME_1), new WalletCreateDTO(NAME_2), new WalletCreateDTO(NAME_3), new WalletCreateDTO(NAME_4), new WalletCreateDTO(NAME_5));
        expectedWallets.forEach(walletDTO -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post("/api/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(walletDTO))))
                        .andExpect(status().isCreated());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                 .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(5))
                 .andDo(print());
    }

}
