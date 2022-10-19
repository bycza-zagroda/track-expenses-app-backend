package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotDeletedException;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.exception.WalletNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Optional;
import java.util.List;
import java.util.Optional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThrows;
import static pl.byczazagroda.trackexpensesappbackend.exception.WalletExceptionMessages.WALLETS_LIST_NOT_FOUND_EXC_MSG;

@WebMvcTest
        (controllers = WalletController.class,
                includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        WalletRepository.class,
                        WalletServiceImpl.class}))
class WalletServiceImplTest {

    private static final String NAME_OF_WALLET = "nameOfWallet";

    private static final String NAME_OF_WALLET_1 = "nameOfWallet1";

    private static final String NAME_OF_WALLET_2 = "nameOfWallet2";

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @MockBean
    private WalletModelMapper walletModelMapper;

    @Test
    void itShouldUpdateWalletName() {
        // given
        UpdateWalletDTO updateWalletDto = new UpdateWalletDTO(1L, "walletName");
        Wallet wallet = new Wallet("anyName");
        wallet.setId(1L);
        Instant time = Instant.now();
        wallet.setCreationDate(Instant.now());
        WalletDTO newWallet = new WalletDTO(1L, "walletName", time);
        given(walletRepository.findById(updateWalletDto.id()))
                .willReturn(Optional.of(wallet));
        given(walletModelMapper.mapWalletEntityToWalletDTO(Mockito.any(Wallet.class))).willReturn(newWallet);

        // when
        WalletDTO walletDTO = walletService.updateWallet(updateWalletDto);

        // then
        assertThat(walletDTO.name()).isEqualTo(updateWalletDto.name());
    }

    @Test
    void itShouldThrowWhenWalletNotFound() {
        // given
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        UpdateWalletDTO updateWalletDto = new UpdateWalletDTO(1L, "walletName");

        // when
        // then
        assertThatThrownBy(() -> walletService.updateWallet(updateWalletDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldCreateWalletProperly() {
        // given
        Instant creationTime = Instant.now();
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(NAME_OF_WALLET);
        Wallet wallet = new Wallet(NAME_OF_WALLET);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, NAME_OF_WALLET, creationTime);

        // when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);

        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);
        WalletDTO returnedWallet = walletService.createWallet(createWalletDTO);

        // then
        Assertions.assertEquals(wallet.getId(), returnedWallet.id());
        Assertions.assertEquals(wallet.getName(), returnedWallet.name());
        Assertions.assertEquals(wallet.getCreationDate(), returnedWallet.creationDate());
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsEmpty() throws Exception {
        // given
        Instant creationTime = Instant.now();
        String emptyName = "  ";
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(emptyName);
        Wallet wallet = new Wallet(emptyName);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, emptyName, creationTime);

        // when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(createWalletDTO));
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsNull() throws Exception {
        // given
        Instant creationTime = Instant.now();
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(null);
        Wallet wallet = new Wallet(null);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, null, creationTime);

        // when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(createWalletDTO));
    }

    @Test
    void shouldThrowAnExceptionWhenNameIsTooLong() throws Exception {
        // given
        Instant creationTime = Instant.now();
        String tooLongName = "This wallet name is too long, it contains over 20 characters";
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(tooLongName);
        Wallet wallet = new Wallet(tooLongName);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, tooLongName, creationTime);

        // when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(createWalletDTO));
    }

    @Test
    void shouldThrowAnExceptionWhenNameContainsIllegalLetters() throws Exception {
        // given
        Instant creationTime = Instant.now();
        String illegalLettersName = "@#$";
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(illegalLettersName);
        Wallet wallet = new Wallet(illegalLettersName);
        long id = 1L;
        wallet.setId(id);
        wallet.setCreationDate(creationTime);
        WalletDTO walletDTO = new WalletDTO(id, illegalLettersName, creationTime);

        // when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(id)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.createWallet(createWalletDTO));
    }

    @Test
    void shouldReturnListOfWalletDTOWithProperSize() {
        // given
        List<Wallet> walletList = createListOfWallets();

        // when
        when(walletRepository.findAll()).thenReturn(walletList);
        List<WalletDTO> allWallets = walletService.getWallets();

        // then
        assertThat(allWallets, hasSize(walletList.size()));
    }

    @Test
    void shouldThrowExceptionWhenListOfWalletsNotFound() {
        // given
        Mockito.when(walletRepository.findAll()).thenThrow(RuntimeException.class);

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> walletService.getWallets());

        // then
        assertThat(exception)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(WALLETS_LIST_NOT_FOUND_EXC_MSG);
    }

    @Test
    void shouldDeletedWalletProperly() {
        //given
        Wallet wallet = new Wallet(NAME_OF_WALLET);
        Long id = 1L;
        Instant creationTime = Instant.now();
        wallet.setId(id);
        wallet.setCreationDate(creationTime);

        //when
        when(walletRepository.existsById(id)).thenReturn(true);
        walletService.deleteWalletById(id);

        //then
        verify(walletRepository).deleteById(wallet.getId());
    }

    @Test
    void shouldThrowAnExceptionWhenWalletWithIdDoesNotExist() throws Exception {
        Wallet wallet = new Wallet(NAME_OF_WALLET);
        Long id = 1L;
        Instant creationTime = Instant.now();
        wallet.setId(id);
        wallet.setCreationDate(creationTime);

        //when
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> walletService.deleteWalletById(5L))
                .isInstanceOf(ResourceNotDeletedException.class);
        assertThatExceptionOfType(ResourceNotDeletedException.class)
                .isThrownBy(()->walletService.deleteWalletById(5L))
                .withMessage("Value does not exist in the database, please change your request");
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

    private List<Wallet> createListOfWallets() {
        Wallet wallet1 = new Wallet(NAME_OF_WALLET);
        Wallet wallet2 = new Wallet(NAME_OF_WALLET_1);
        Wallet wallet3 = new Wallet(NAME_OF_WALLET_2);
        return List.of(wallet1, wallet2, wallet3);
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
    void shouldThrowNoSuchElementException() {
        long id = 1L;

        Assertions.assertThrows(WalletNotFoundException.class, () -> walletService.findById(id));
    }
}

