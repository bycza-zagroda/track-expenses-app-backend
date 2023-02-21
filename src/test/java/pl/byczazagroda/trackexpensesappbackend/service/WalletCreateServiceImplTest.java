package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.validation.ConstraintViolationException;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class}))
class WalletCreateServiceImplTest {

    public static final long ID_1L = 1L;

    private static final String NAME_1 = "wallet name one";

    private static final String NAME_EMPTY = "";

    private static final String NAME_BLANK = " ";

    public static final String INVALID_NAME = "@#$%^&";

    private static final Instant DATE_NOW = Instant.now();

    public static final String TOO_LONG_NAME_MORE_THAN_20_LETTERS = "Too long name - more than 20 letters.";

    @MockBean
    private ErrorStrategy errorStrategy;

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @MockBean
    private WalletModelMapper walletModelMapper;

    @Test
    @DisplayName("when wallet data are correct should create wallet successfully")
    void shouldCreateWalletSuccessfully_WhenWalletDataAreCorrect() {
        // given
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(NAME_1);
        Wallet wallet = new Wallet(NAME_1);
        wallet.setId(ID_1L);
        wallet.setCreationDate(DATE_NOW);
        WalletDTO walletDTO = new WalletDTO(ID_1L, NAME_1, DATE_NOW);

        // when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(ID_1L)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);
        WalletDTO returnedWalletDTO = walletService.createWallet(walletCreateDTO);

        // then
        Assertions.assertEquals(wallet.getId(), returnedWalletDTO.id());
        Assertions.assertEquals(wallet.getName(), returnedWalletDTO.name());
        Assertions.assertEquals(wallet.getCreationDate(), returnedWalletDTO.creationDate());
    }

    @Test
    @DisplayName("when wallet name is empty should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsEmpty() {
        // given
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(NAME_EMPTY);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO));
    }

    @Test
    @DisplayName("when wallet name is blank should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsBlank() {
        // given
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(NAME_BLANK);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO));
    }

    @Test
    @DisplayName("when wallet name is null should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsNull() {
        // given
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(null);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO));
    }

    @Test
    @DisplayName("when wallet name is too long should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsTooLong() {
        // given
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(TOO_LONG_NAME_MORE_THAN_20_LETTERS);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO));
    }

    @Test
    @DisplayName("when wallet name contains illegal letters should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameContainsIllegalLetters() {
        // given
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(INVALID_NAME);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO));
    }
}
