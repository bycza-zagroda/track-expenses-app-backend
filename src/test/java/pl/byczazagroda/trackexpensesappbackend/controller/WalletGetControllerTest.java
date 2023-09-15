package pl.byczazagroda.trackexpensesappbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.byczazagroda.trackexpensesappbackend.config.WebSecurityConfig;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;
import pl.byczazagroda.trackexpensesappbackend.service.WalletServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {WalletModelMapper.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
class WalletGetControllerTest {

    public static final long ID_100L = 100L;
    private static final Long WALLET_ID_0L = 0L;
    private static final Long WALLET_ID_1L = 1L;
    private static final Long USER_ID_1L = 1L;
    private static final Long WALLET_ID_2L = 2L;
    private static final Long USER_ID_2L = 2L;
    private static final Long WALLET_ID_3L = 3L;
    private static final Long USER_ID_3L = 3L;
    private static final String WALLET_NAME = "Wallet name";

    private static final Instant DATE_1 = Instant.parse("2022-09-24T19:09:35.573036Z");

    private static final Instant DATE_2 = Instant.parse("2022-09-25T17:10:39.684145Z");

    private static final Instant DATE_3 = Instant.parse("2022-09-26T18:11:49.132454Z");

    private static final Instant DATE_NOW = Instant.now();

    private static final String LIST_OF_WALLETS_HEADER_MSG = "The list of wallets has been successfully retrieved.";

    private static final String EMPTY_LIST_OF_WALLETS_HEADER_MSG = "There are no available wallets to view.";

    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("when finding wallet list return empty list and response status OK")
    void shouldResponseStatusOKAndReturnEmptyList_whenUserHasNoWallets() throws Exception {
        // when
        MockHttpServletResponse result = mockMvc.perform(get("/api/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse();

        // then
        Assertions.assertAll(
                () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result.getContentAsString()).isEqualTo(Collections.emptyList().toString())
        );
    }

    @Test
    @DisplayName("when finding all wallets should return wallets list and response status OK")
    void shouldResponseStatusOKAndAllWalletsList_whenUserHasWallets() throws Exception {
        // given
        List<WalletDTO> listDTO = createListOfWalletsDTO();
        given(walletService.getWallets(USER_ID_1L))
                .willReturn(listDTO);
        // when

        // then
        mockMvc.perform(get("/api/wallets")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(listDTO.size()));
// Value of returned items should be greater than 0 when testing Controller for unit(!) test
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ID_OF_WALLET_1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(NAME_OF_WALLET_1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].creationDate").value(CREATION_DATE_OF_WALLET_1.toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(ID_OF_WALLET_2))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(NAME_OF_WALLET_2))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].creationDate").value(CREATION_DATE_OF_WALLET_2.toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(ID_OF_WALLET_3))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(NAME_OF_WALLET_3))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[2].creationDate").value(CREATION_DATE_OF_WALLET_3.toString()));
    }

    @Test
    @DisplayName("when finding wallet by id should return wallet and response status OK")
    void shouldReturnResponseStatusOKAndWallet_WhenFindWalletById() throws Exception {
        // given
        WalletDTO wallet = new WalletDTO(WALLET_ID_1L, WALLET_NAME, DATE_NOW, USER_ID_1L);

        // when
        when(walletService.findById(WALLET_ID_1L, USER_ID_1L)).thenReturn(wallet);
        ResultActions resultActions = mockMvc.perform(get("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(wallet)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(WALLET_ID_1L.intValue()));
    }

    @Test
    @DisplayName("when finding wallet by id does not exist should return response status not found")
    void shouldReturnResponseStatusNotFound_WhenWalletByIdDoesNotExist() throws Exception {
        // given
        WalletDTO wallet = new WalletDTO(WALLET_ID_1L, "", DATE_NOW, USER_ID_1L);
        doThrow(new AppRuntimeException(ErrorCode.W003, ""))
                .when(walletService).findById(ID_100L, USER_ID_1L);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/wallets/{id}", ID_100L)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("when finding wallet by id zero should return response status no content")
    void shouldReturnResponseStatusNoContent_WhenFindWalletByIdZero() throws Exception {
        //given
        WalletDTO walletDTO = new WalletDTO(WALLET_ID_1L, WALLET_NAME, DATE_NOW, USER_ID_1L);
        doThrow(ConstraintViolationException.class)
                .when(walletService).findById(WALLET_ID_0L, USER_ID_1L);

        //when
        ResultActions result = mockMvc.perform(get("/api/wallets/{id}", WALLET_ID_0L)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        //then
        result.andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("when finding wallet by name should return wallets and response status OK")
    void shouldReturnResponseStatusOKAndWallets_WhenFindWalletByName() throws Exception {
        // given
        String walletNameSearched = WALLET_NAME;
        List<WalletDTO> listOfWalletsDTO = createListOfWalletsDTO();
        List<WalletDTO> foundedWalletsDTO = List.of(new WalletDTO(WALLET_ID_2L, WALLET_NAME, DATE_2, USER_ID_2L));
        given(walletService.getWallets(USER_ID_1L)).willReturn(listOfWalletsDTO);
        given(walletService.findAllByNameIgnoreCase(walletNameSearched, USER_ID_1L)).willReturn(foundedWalletsDTO);

        // when

        // then
        mockMvc.perform(get("/api/wallets/wallets/{name}", walletNameSearched)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(foundedWalletsDTO.size()));
// Value of returned items should be greater than 0 when testing Controller for unit(!) test
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ID_OF_WALLET_2))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(NAME_OF_WALLET_2))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].creationDate").value(CREATION_DATE_OF_WALLET_2.toString()));
    }

    private List<WalletDTO> createListOfWalletsDTO() {
        WalletDTO walletDTO1 = new WalletDTO(WALLET_ID_1L, WALLET_NAME, DATE_1, USER_ID_1L);
        WalletDTO walletDTO2 = new WalletDTO(WALLET_ID_2L, WALLET_NAME, DATE_2, USER_ID_2L);
        WalletDTO walletDTO3 = new WalletDTO(WALLET_ID_3L, WALLET_NAME, DATE_3, USER_ID_3L);

        return List.of(walletDTO1, walletDTO2, walletDTO3);
    }
}
