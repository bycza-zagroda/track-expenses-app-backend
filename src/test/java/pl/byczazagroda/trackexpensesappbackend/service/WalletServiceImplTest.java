package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest
        (controllers = WalletController.class,
                includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        WalletRepository.class,
                        WalletServiceImpl.class}))
class WalletServiceImplTest {

    private static final String NAME_OF_WALLET = "nameOfWallet";

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @MockBean
    private WalletModelMapper walletModelMapper;


    @Test
    void shouldCreateWalletProperly() {
//given
        Instant creationTime = Instant.now();
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(NAME_OF_WALLET);
        Wallet wallet = new Wallet(NAME_OF_WALLET);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, NAME_OF_WALLET, creationTime);

//        when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);

        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);
        WalletDTO returnedWallet = walletService.createWallet(createWalletDTO);

//        then
        Assertions.assertEquals(wallet.getId(), returnedWallet.id());
        Assertions.assertEquals(wallet.getName(), returnedWallet.name());
        Assertions.assertEquals(wallet.getCreationDate(), returnedWallet.creationDate());
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsEmpty() throws Exception {
//        given
        Instant creationTime = Instant.now();
        String emptyName = "  ";
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(emptyName);
        Wallet wallet = new Wallet(emptyName);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, emptyName, creationTime);

//        when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);

//        then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(createWalletDTO));
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsNull() throws Exception {
//        given
        Instant creationTime = Instant.now();
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(null);
        Wallet wallet = new Wallet(null);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, null, creationTime);

//        when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);

//        then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(createWalletDTO));
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsTooLong() throws Exception {
//        given
        Instant creationTime = Instant.now();
        String tooLongName = "This wallet name is too long, it contains over 20 characters";
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(tooLongName);
        Wallet wallet = new Wallet(tooLongName);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, tooLongName, creationTime);

//        when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);

//        then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(createWalletDTO));
    }

    @Test
    void shouldThrowAnExceptionWhenNameContainsIllegalLetters() throws Exception {
//        given
        Instant creationTime = Instant.now();
        String illegalLettersName = "@#$";
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(illegalLettersName);
        Wallet wallet = new Wallet(illegalLettersName);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, illegalLettersName, creationTime);

//        when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);

//        then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(createWalletDTO));
    }

    @Test
    void shouldFindWalletProperly() {
        //given
        Instant creationTime = Instant.now();
        Wallet wallet = new Wallet(NAME_OF_WALLET);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO expectedWallet = new WalletDTO(id, NAME_OF_WALLET, creationTime);
        //when
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(expectedWallet);
        //then
        WalletDTO actualWallet = walletService.findById(1L);

        Assertions.assertEquals(expectedWallet, actualWallet);
    }

    @Test
    void shouldThrowResourceNotFoundException() {
        long id = 1L;

        Assertions.assertThrows(ResourceNotFoundException.class, () -> walletService.findById(id));
    }
}
