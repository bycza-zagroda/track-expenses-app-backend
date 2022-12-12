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
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotDeletedException;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class}))
class WalletServiceImplTest {

    public static final long ID_1L = 1L;

    public static final long ID_5L = 5L;

    private static final String NAME_1 = "wallet name one";

    private static final String NAME_OF_WALLET_1 = "nameOfWallet1";

    private static final String NAME_OF_WALLET_2 = "nameOfWallet2";

    public static final String INVALID_NAME = "@#$%^&";

    private static final Instant DATE_NOW = Instant.now();

    public static final String TOO_LONG_NAME_MORE_THAN_20_LETTERS = "Too long name - more than 20 letters.";

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @MockBean
    private WalletModelMapper walletModelMapper;


    @Test
    void itShouldUpdateWalletName() {
        // given
        UpdateWalletDTO updateWalletDto = new UpdateWalletDTO(ID_1L, NAME_1);
        Wallet wallet = new Wallet(NAME_2);
        wallet.setId(ID_1L);
        wallet.setCreationDate(DATE_NOW);
        WalletDTO newWalletDTO = new WalletDTO(ID_1L, NAME_1, DATE_NOW);
        given(walletRepository.findById(updateWalletDto.id())).willReturn(Optional.of(wallet));
        given(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).willReturn(newWalletDTO);

        // when
        WalletDTO walletDTO = walletService.update(updateWalletDto);

        // then
        assertThat(walletDTO.name()).isEqualTo(updateWalletDto.name());
    }

    @Test
    @DisplayName("when wallet id doesn't exist should not return wallet")
    void shouldNotReturnWalletById_WhenWalletIdNotExist() {
        // given
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        UpdateWalletDTO updateWalletDto = new UpdateWalletDTO(ID_1L, NAME_1);

        // when

        // then
        assertThatThrownBy(() -> walletService.update(updateWalletDto)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("when wallet data are correct should create wallet successfully")
    void shouldCreateWalletSuccessfully_WhenWalletDataAreCorrect() {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(NAME_1);
        Wallet wallet = new Wallet(NAME_1);
        wallet.setId(ID_1L);
        wallet.setCreationDate(DATE_NOW);
        WalletDTO walletDTO = new WalletDTO(ID_1L, NAME_1, DATE_NOW);

        // when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(ID_1L)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);
        WalletDTO returnedWalletDTO = walletService.create(createWalletDTO);

        // then
        Assertions.assertEquals(wallet.getId(), returnedWalletDTO.id());
        Assertions.assertEquals(wallet.getName(), returnedWalletDTO.name());
        Assertions.assertEquals(wallet.getCreationDate(), returnedWalletDTO.creationDate());
    }

    @Test
    @DisplayName("when wallet name is empty should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsEmpty() {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(NAME_EMPTY);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.create(createWalletDTO));
    }

    @Test
    @DisplayName("when wallet name is null should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsNull() {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(null);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.create(createWalletDTO));
    }

    @Test
    @DisplayName("when wallet name is too long should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameIsTooLong() {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(TOO_LONG_NAME_MORE_THAN_20_LETTERS);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.create(createWalletDTO));
    }

    @Test
    @DisplayName("when wallet name contains illegal letters should not create wallet")
    void shouldNotCreateWallet_WhenWalletNameContainsIllegalLetters() {
        // given
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(INVALID_NAME);

        // when

        // then
        Assertions.assertThrows(ConstraintViolationException.class, () -> walletService.create(createWalletDTO));
    }

    @Test
    @DisplayName("when wallet with id does not exist should not delete wallet")
    void shouldNotDeleteWallet_WhenWalletWithIdDoesNotExist() {
        //given
        Wallet wallet = new Wallet(NAME_1);
        wallet.setId(ID_1L);
        wallet.setCreationDate(DATE_NOW);

        //when
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> walletService.deleteById(ID_5L)).isInstanceOf(ResourceNotDeletedException.class);
        assertThatExceptionOfType(ResourceNotDeletedException.class).isThrownBy(() -> walletService.deleteById(ID_5L)).withMessage("Value does not exist in the database, please change your request");
    }

    @Test
    @DisplayName("when finding with proper wallet id should successfully find wallet")
    void shouldSuccessfullyFindWallet_WhenFindingWithProperWalletId() {
        //given
        Wallet wallet = new Wallet(NAME_1);
        wallet.setId(ID_1L);
        wallet.setCreationDate(DATE_NOW);
        WalletDTO expectedDTO = new WalletDTO(ID_1L, NAME_1, DATE_NOW);

        //when
        when(walletRepository.findById(ID_1L)).thenReturn(Optional.of(wallet));
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(expectedDTO);
        WalletDTO foundWallet = walletService.findById(ID_1L);

        //then
        Assertions.assertEquals(expectedDTO, foundWallet);
    }

    @Test
    @DisplayName("when wallet by id not found should not return wallet")
    void shouldNotReturnWallet_WhenWalletByIdNotFound() {
        //given
        Wallet wallet = new Wallet(NAME_1);
        wallet.setId(ID_1L);
        wallet.setCreationDate(DATE_NOW);

        //when
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> walletService.findById(ID_5L)).isInstanceOf(ResourceNotFoundException.class);
        assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() ->
                        walletService.findById(ID_5L)).withMessage("Wallet with that id doesn't exist");
    }

    @Test
    @DisplayName("Should return list of WalletDTO by name with proper size")
    void shouldReturnListOfWalletDTOByNameWithProperSize() {
        // given
        String walletNameSearched = "Family";
        List<Wallet> walletList = createListOfWalletsByName("Family wallet", "Common Wallet", "Smith Family Wallet");
        List<WalletDTO> walletListDTO = walletList.stream().map((Wallet x) -> new WalletDTO(x.getId(), x.getName(), x.getCreationDate())).toList();
        given(walletRepository.findAll()).willReturn(walletList);
        walletList.forEach(wallet -> given(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).willReturn(
                walletListDTO.stream().filter(walletDTO -> Objects.equals(wallet.getName(), walletDTO.name())).findAny().orElse(null)));

        // when
        List<WalletDTO> fundedWallets = walletService.findAllByNameLikeIgnoreCase(walletNameSearched);

        // then
        assertThat(fundedWallets, hasSize(walletRepository.findAllByNameLikeIgnoreCase(walletNameSearched).size()));
    }

    private List<Wallet> createListOfWalletsByName(String... name) {
        return Arrays.stream(name).map(Wallet::new).toList();
    }

    private List<Wallet> createListOfWallets() {
        Wallet wallet1 = new Wallet(NAME_OF_WALLET);
        Wallet wallet2 = new Wallet(NAME_OF_WALLET_1);
        Wallet wallet3 = new Wallet(NAME_OF_WALLET_2);
        return List.of(wallet1, wallet2, wallet3);
    }
}

