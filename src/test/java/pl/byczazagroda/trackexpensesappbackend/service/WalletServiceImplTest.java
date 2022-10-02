package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.dto.EditWalletDto;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.exception.WalletNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletModelMapper mapper;

    @Autowired
    private WalletServiceImpl underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new WalletServiceImpl(walletRepository, mapper);
    }

    @Test
    void itShouldUpdateWalletName() {
        //GIVEN
        EditWalletDto editWalletDto = returnEditWalletDto();

        given(walletRepository.findById(editWalletDto.id()))
                .willReturn(Optional.of(new Wallet("anyName")));
        //WHEN
        WalletDTO walletDTO = underTest.updateWallet(editWalletDto);
        //THEN
        assertThat(walletDTO.name()).isEqualTo(editWalletDto.name());
    }

    @Test
    void itShouldThrowWhenWalletNotFound() {
        //GIVEN
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.updateWallet(Mockito.any()))
                .isInstanceOf(WalletNotFoundException.class);
    }

    EditWalletDto returnEditWalletDto() {
        return new EditWalletDto(1L, "walletName");
    }
}