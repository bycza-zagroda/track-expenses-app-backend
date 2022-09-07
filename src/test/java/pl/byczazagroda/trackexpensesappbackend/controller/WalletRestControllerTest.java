package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.byczazagroda.trackexpensesappbackend.BaseControllerTest;
import pl.byczazagroda.trackexpensesappbackend.model.dto.CreateWalletDto;
import pl.byczazagroda.trackexpensesappbackend.service.WalletServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:data-test.properties")
class WalletRestControllerTest extends BaseControllerTest {

  @MockBean
  WalletServiceImpl walletService;

  @SneakyThrows
  @Test
  void shouldReturnCreatedCodeCodeWhenWalletCreated() {

    CreateWalletDto createWalletDto = new CreateWalletDto("Test wallet name");

    mockMvc.perform(post("/api/v1/wallets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(createWalletDto)))
        .andExpect(status().isCreated());
  }
}
