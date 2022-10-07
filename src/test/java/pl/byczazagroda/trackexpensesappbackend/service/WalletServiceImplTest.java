package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDto;
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
        UpdateWalletDto updateWalletDto = returnEditWalletDto();

        given(walletRepository.findById(updateWalletDto.id()))
                .willReturn(Optional.of(new Wallet("anyName")));
        //WHEN
        WalletDTO walletDTO = underTest.updateWallet(updateWalletDto);
        //THEN
        assertThat(walletDTO.name()).isEqualTo(updateWalletDto.name());
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

    UpdateWalletDto returnEditWalletDto() {
        return new UpdateWalletDto(1L, "walletName");
    }
}