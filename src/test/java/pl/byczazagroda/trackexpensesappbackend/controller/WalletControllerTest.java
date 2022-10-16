package pl.byczazagroda.trackexpensesappbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.service.WalletServiceImpl;

import java.time.Instant;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                WalletModelMapper.class}))
class WalletControllerTest {

    @MockBean
    private WalletServiceImpl walletService;

    @Autowired
    private WalletController underTest;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldReturnStatusOKAndCorrectResponseBody() throws Exception {
        //GIVEN
        Instant timeCreated = Instant.now();

        TestEditWalletDto editWalletDto = returnTestCorrectEditWalletDto();
        given(walletService.updateWallet(Mockito.any())).willReturn(
                new WalletDTO(
                        editWalletDto.getId(),
                        editWalletDto.getName(),
                        timeCreated
                )
        );

        //WHEN
        ResultActions editResultActions = mockMvc.perform(
                patch("/api/wallet/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(editWalletDto)))
        );

        //THEN
        editResultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(editWalletDto.id)))
                .andExpect(jsonPath("$.name", Matchers.equalTo(editWalletDto.name)))
                .andExpect(jsonPath("$.creation_date", Matchers.equalTo(timeCreated)));

    }

    @Test
    void itShouldReturnBadRequestWhenTooLongName() throws Exception {
        //GIVEN
        TestEditWalletDto editWalletDto = returnTestEditWalletDtoWithTooLongName();
        given(walletService.updateWallet(Mockito.any())).willReturn(
                new WalletDTO(
                        Mockito.anyLong(),
                        Mockito.anyString(),
                        Instant.now()
                )
        );

        //WHEN
        ResultActions editResultActions = mockMvc.perform(
                patch("/api/wallet/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(editWalletDto)))
        );

        //THEN
        editResultActions.andExpect(status().isBadRequest());
    }

    @Test
    void itShouldReturnBadRequestWhenNameIsEmpty() throws Exception {
        //GIVEN
        TestEditWalletDto editWalletDto = returnTestEditWalletDtoWithEmptyName();
        given(walletService.updateWallet(Mockito.any())).willReturn(
                new WalletDTO(
                        Mockito.anyLong(),
                        Mockito.anyString(),
                        Instant.now()
                )
        );

        //WHEN
        ResultActions editResultActions = mockMvc.perform(
                patch("/api/wallet/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(editWalletDto)))
        );

        //THEN
        editResultActions.andExpect(status().isBadRequest());
    }

    @Test
    void itShouldReturnBadRequestWhenNameContainsWrongLetters() throws Exception {
        //GIVEN
        TestEditWalletDto editWalletDto = returnTestEditWalletDtoWithWrongLetterInName();
        given(walletService.updateWallet(Mockito.any())).willReturn(
                new WalletDTO(
                        Mockito.anyLong(),
                        Mockito.anyString(),
                        Instant.now()
                )
        );

        //WHEN
        ResultActions editResultActions = mockMvc.perform(
                patch("/api/wallet/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(editWalletDto)))
        );

        //THEN
        editResultActions.andExpect(status().isBadRequest());
    }

    @Test
    void itShouldReturnBadRequestWhenIdIsEmpty() throws Exception {
        //GIVEN
        TestEditWalletDto editWalletDto = returnTestEditWalletDtoWithNoId();
        given(walletService.updateWallet(Mockito.any())).willReturn(
                new WalletDTO(
                        Mockito.anyLong(),
                        Mockito.anyString(),
                        Instant.now()
                )
        );

        //WHEN
        ResultActions editResultActions = mockMvc.perform(
                patch("/api/wallet/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(editWalletDto)))
        );

        //THEN
        editResultActions.andExpect(status().isBadRequest());
    }

    @Test
    void itShouldReturnBadRequestWhenIdIsZero() throws Exception {
        //GIVEN
        TestEditWalletDto editWalletDto = returnTestEditWalletDtoWithIdZero();
        given(walletService.updateWallet(Mockito.any())).willReturn(
                new WalletDTO(
                        Mockito.anyLong(),
                        Mockito.anyString(),
                        Instant.now()
                )
        );

        //WHEN
        ResultActions editResultActions = mockMvc.perform(
                patch("/api/wallet/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(editWalletDto)))
        );

        //THEN
        editResultActions.andExpect(status().isBadRequest());
    }

    private TestEditWalletDto returnTestCorrectEditWalletDto() {
        return new TestEditWalletDto(1L, "correctEditWalletDto");
    }

    private TestEditWalletDto returnTestEditWalletDtoWithTooLongName() {
        return new TestEditWalletDto(1L, "correctEditWalletDtoTooLongNameBlaBlaBla");
    }

    private TestEditWalletDto returnTestEditWalletDtoWithWrongLetterInName() {
        return new TestEditWalletDto(1L, "EditWalletDto123!@#$%");
    }

    private TestEditWalletDto returnTestEditWalletDtoWithEmptyName() {
        TestEditWalletDto testWallet = new TestEditWalletDto();
        testWallet.setId(1L);
        return testWallet;
    }

    private TestEditWalletDto returnTestEditWalletDtoWithNoId() {
        TestEditWalletDto testWallet = new TestEditWalletDto();
        testWallet.setName("correctName");
        return testWallet;
    }

    private TestEditWalletDto returnTestEditWalletDtoWithIdZero() {
        TestEditWalletDto testWallet = new TestEditWalletDto();
        testWallet.setId(0L);
        testWallet.setName("correctName");
        return testWallet;
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to Json");
            return null;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class TestEditWalletDto {
        private Long id;
        private String name;
    }
}