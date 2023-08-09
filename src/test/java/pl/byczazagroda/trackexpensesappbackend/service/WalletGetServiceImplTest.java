package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class}))
class WalletGetServiceImplTest {

    public static final long WALLET_ID_1L = 1L;

    public static final long USER_ID_1L = 1L;

    public static final long ID_5L = 5L;

    private static final String NAME_1 = "wallet name one";

    private static final Instant DATE_NOW = Instant.now();

    @MockBean
    private ErrorStrategy errorStrategy;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @MockBean
    private WalletModelMapper walletModelMapper;

    @Test
    @DisplayName("when wallet id doesn't exist should not return wallet")
    void shouldNotReturnWalletById_WhenWalletIdNotExist() {
        // given
        Long userId = 1L;
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        WalletUpdateDTO walletUpdateDto = new WalletUpdateDTO(NAME_1);

        // when

        // then
        assertThatThrownBy(() -> walletService.updateWallet(WALLET_ID_1L, walletUpdateDto, userId)).isInstanceOf(AppRuntimeException.class);
    }

    @Test
    @DisplayName("when finding with proper wallet id should successfully find wallet")
    void shouldSuccessfullyFindWallet_WhenFindingWithProperWalletId() {
        //given
        Wallet wallet = Wallet.builder()
                .id(WALLET_ID_1L)
                .name(NAME_1)
                .creationDate(DATE_NOW)
                .user(createTestUser())
                .build();

        WalletDTO expectedDTO = new WalletDTO(WALLET_ID_1L, NAME_1, DATE_NOW, USER_ID_1L);

        //when
        when(walletRepository.existsById(WALLET_ID_1L)).thenReturn(true);
        when(walletRepository.findById(WALLET_ID_1L)).thenReturn(Optional.of(wallet));
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(expectedDTO);

        WalletDTO foundWallet = walletService.findById(WALLET_ID_1L, USER_ID_1L);

        //then
        Assertions.assertEquals(expectedDTO, foundWallet);
    }

    @Test
    @DisplayName("when wallet by id not found should not return wallet")
    void shouldNotReturnWallet_WhenWalletByIdNotFound() {
        //given

        //when
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> walletService.findById(ID_5L, USER_ID_1L)).isInstanceOf(AppRuntimeException.class);
        assertThatExceptionOfType(AppRuntimeException.class).isThrownBy(() ->
                walletService.findById(ID_5L, USER_ID_1L)).withMessage(ErrorCode.W003.getBusinessMessage());
    }

    @Test
    @DisplayName("when finding wallet by name should return all wallets contains this name pattern")
    void shouldReturnAllWalletsContainsNamePattern_WhenFindingWalletByName() {
        // given
        User user = createTestUser();
        String walletNameSearched = "Family";
        List<Wallet> walletList = createListOfWalletsByName(user,
                "Family wallet", "Common Wallet", "Smith Family Wallet", "Random family wallet");
        List<WalletDTO> walletListDTO = walletList.stream().map((Wallet x) ->
                new WalletDTO(x.getId(), x.getName(), x.getCreationDate(), 1L)).toList();
        given(walletRepository.findAll()).willReturn(walletList);


        given(walletRepository.findAllByUserIdAndNameIsContainingIgnoreCase(USER_ID_1L, walletNameSearched))
                .willReturn(walletList.stream()
                                .filter(wallet -> wallet.getName().contains(walletNameSearched))
                                .toList());



        walletList.forEach(wallet -> given(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).willReturn(
                walletListDTO
                        .stream()
                        .filter(walletDTO -> wallet.getName().toUpperCase().contains(walletNameSearched.toUpperCase()))
                        .findAny()
                        .orElse(null)));
        // when
        List<WalletDTO> fundedWallets = walletService.findAllByNameIgnoreCase(walletNameSearched, USER_ID_1L);

        // then
        assertThat(fundedWallets, hasSize(walletRepository.findAllByUserIdAndNameIsContainingIgnoreCase(USER_ID_1L, walletNameSearched).size()));
    }

    private List<Wallet> createListOfWalletsByName(User user, String... names) {
        return Arrays.stream(names).map(name -> new Wallet(name, user)).toList();
    }

    private User createTestUser() {
        return User.builder()
                .id(USER_ID_1L)
                .userName("userone")
                .email("Email@wp.pl")
                .password("password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
    }
}
