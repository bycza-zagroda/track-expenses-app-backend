package pl.byczazagroda.trackexpensesappbackend.service;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class, WalletModelMapper.class, UserRepository.class}))
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WalletCreateServiceImplTest {

    private static final Long ID_1L = 1L;

    private static final String BLANK_WALLET_NAME = " ";

    private static final String EMPTY_WALLET_NAME = "";

    private static final String INVALID_WALLET_NAME = "@#$%^&";

    private static final String TOO_LONG_NAME_MORE = "Too long name - more than 20 letters.";

    private static final String WALLET_NAME = "Wallet example name";

    private static final Instant DATE_NOW = Instant.now();

    @MockBean
    private ErrorStrategy errorStrategy;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @MockBean
    private WalletModelMapper walletModelMapper;

    @Test
    @DisplayName("when wallet data are correct should create wallet successfully")
    void shouldCreateWalletSuccessfully_WhenWalletDataAreCorrect() {
        //given
        User user = createTestUser();

        Wallet wallet = createTestWalletForUser(user);
        wallet.setName(WALLET_NAME);

        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(WALLET_NAME);

        wallet.setCreationDate(DATE_NOW);
        WalletDTO walletDTO = new WalletDTO(ID_1L, WALLET_NAME, DATE_NOW, ID_1L);

        // when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(ID_1L)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        WalletDTO returnedWalletDTO = walletService.createWallet(walletCreateDTO, user.getId());

        // then
        Assertions.assertEquals(wallet.getId(), returnedWalletDTO.id());
        Assertions.assertEquals(wallet.getName(), returnedWalletDTO.name());
        Assertions.assertEquals(wallet.getCreationDate(), returnedWalletDTO.creationDate());
    }

    @Test
    @DisplayName("when wallet name is empty should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsEmpty() {
        //given
        User user = createTestUser();

        Wallet wallet = createTestWalletForUser(user);

        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(EMPTY_WALLET_NAME);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        when(walletRepository.save(any())).thenReturn(wallet);

        // when

        // then
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> walletService.createWallet(walletCreateDTO, user.getId()));
    }

    @Test
    @DisplayName("when wallet name is blank should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsBlank() {
        //given
        User user = createTestUser();

        Wallet wallet = createTestWalletForUser(user);

        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(BLANK_WALLET_NAME);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        when(walletRepository.save(any())).thenReturn(wallet);

        // when

        // then
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> walletService.createWallet(walletCreateDTO, user.getId()));
    }

    @Test
    @DisplayName("when wallet name is null should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsNull() {
        //given
        User user = createTestUser();

        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(null);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO, user.getId()));
    }

    @Test
    @DisplayName("when wallet name is too long should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsTooLong() {
        //given
        User user = createTestUser();

        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(TOO_LONG_NAME_MORE);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO, user.getId()));
    }

    @Test
    @DisplayName("when wallet name contains illegal letters should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameContainsIllegalLetters() {
        //given
        User user = createTestUser();

        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(INVALID_WALLET_NAME);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO, user.getId()));
    }

    private User createTestUser() {
        return User.builder()
                .id(1L)
                .userName("UserOne")
                .email("email@server.com")
                .password("Password1!")
                .userStatus(UserStatus.VERIFIED)
                .build();
    }

    @NotNull
    private static Wallet createTestWalletForUser(User user) {
        Wallet wallet = new Wallet();
        wallet.setId(ID_1L);
        wallet.setUser(user);

        return wallet;
    }

}

