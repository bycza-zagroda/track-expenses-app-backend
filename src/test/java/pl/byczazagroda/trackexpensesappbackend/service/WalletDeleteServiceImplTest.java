package pl.byczazagroda.trackexpensesappbackend.service;


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
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotDeletedException;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = WalletController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {WalletRepository.class, WalletServiceImpl.class}))
class WalletDeleteServiceImplTest {

    public static final long ID_1L = 1L;

    public static final long ID_5L = 5L;

    private static final String NAME_1 = "wallet name one";

    private static final Instant DATE_NOW = Instant.now();

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @Test
    @DisplayName("when wallet with id does not exist should not delete wallet")
    void shouldNotDeleteWallet_WhenWalletWithIdDoesNotExist() {
        //given
        Wallet wallet = new Wallet(NAME_1);
        wallet.setId(ID_1L);
        wallet.setCreationDate(DATE_NOW);

        //when
        when(walletRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> walletService.deleteById(ID_5L)).isInstanceOf(ResourceNotDeletedException.class);
        assertThatExceptionOfType(ResourceNotDeletedException.class).isThrownBy(() -> walletService.deleteById(ID_5L)).withMessage("Value does not exist in the database, please change your request");
    }
}

