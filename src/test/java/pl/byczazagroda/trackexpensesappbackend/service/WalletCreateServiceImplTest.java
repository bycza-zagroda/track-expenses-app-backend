package pl.byczazagroda.trackexpensesappbackend.service;

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
import pl.byczazagroda.trackexpensesappbackend.TestUtils;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.WalletController;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.general.exception.ErrorStrategy;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.auth.usermodel.User;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.auth.api.AuthRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.byczazagroda.trackexpensesappbackend.wallet.impl.WalletServiceImpl;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class, WalletModelMapper.class, AuthRepository.class}))
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WalletCreateServiceImplTest {

    private static final Long ID_1L = 1L;

    private static final String BLANK_WALLET_NAME = " ";

    private static final String EMPTY_WALLET_NAME = "";

    private static final String INVALID_WALLET_NAME = "@#$%^&";

    private static final String TOO_LONG_NAME_MORE = "Too long name - more than 20 letters.";

    private static final Instant DATE_NOW = Instant.now();

    @MockBean
    private FinancialTransactionRepository financialTransactionRepository;

    @MockBean
    private FinancialTransactionModelMapper financialTransactionModelMapper;

    @MockBean
    private ErrorStrategy errorStrategy;

    @MockBean
    private AuthRepository userRepository;

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @MockBean
    private WalletModelMapper walletModelMapper;

    @Test
    @DisplayName("Should create a wallet successfully with valid data")
    void createWallet_ValidData_ReturnsWalletDTO() {
        //given
        User user = TestUtils.createUserForTest();

        Wallet wallet = TestUtils.createWalletForTest(user);
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(wallet.getName());

        wallet.setCreationDate(DATE_NOW);
        WalletDTO walletDTO = new WalletDTO(wallet.getId(), wallet.getName(), wallet.getCreationDate(), wallet.getUser().getId());

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
    @DisplayName("Should not create a wallet with an empty name")
    void createWallet_EmptyName_ThrowConstraintViolationException() {
        //given
        User user = TestUtils.createUserForTest();
        Long userId = user.getId();

        Wallet wallet = TestUtils.createWalletForTest(user);
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(EMPTY_WALLET_NAME);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        when(walletRepository.save(any())).thenReturn(wallet);

        // when

        // then
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> walletService.createWallet(walletCreateDTO, userId));
    }

    @Test
    @DisplayName("Should not create a wallet with a blank name")
    void createWallet_BlankName_ThrowConstraintViolationException() {
        //given
        User user = TestUtils.createUserForTest();
        Long userId = user.getId();

        Wallet wallet = TestUtils.createWalletForTest(user);
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(BLANK_WALLET_NAME);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        when(walletRepository.save(any())).thenReturn(wallet);

        // when

        // then
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> walletService.createWallet(walletCreateDTO, userId));
    }

    @Test
    @DisplayName("Should not create a wallet with a null name")
    void createWallet_NullName_ThrowConstraintViolationException() {
        //given
        Long userId = TestUtils.createUserForTest().getId();
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(null);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO, userId));
    }

    @Test
    @DisplayName("Should not create a wallet with a too long name")
    void createWallet_TooLongName_ThrowConstraintViolationException() {
        //given
        Long userId = TestUtils.createUserForTest().getId();

        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(TOO_LONG_NAME_MORE);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO, userId));
    }

    @Test
    @DisplayName("Should not create a wallet with illegal characters in the name")
    void createWallet_IllegalNameCharacters_ThrowConstraintViolationException() {
        //given
        Long userId = TestUtils.createUserForTest().getId();
        WalletCreateDTO walletCreateDTO = new WalletCreateDTO(INVALID_WALLET_NAME);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(walletCreateDTO, userId));
    }

}
