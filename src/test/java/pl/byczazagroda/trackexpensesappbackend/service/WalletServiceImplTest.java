package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@WebMvcTest
        (controllers = WalletController.class,
                includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        WalletRepository.class,
                        WalletServiceImpl.class}))
class WalletServiceImplTest {

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private WalletModelMapper mapper;

    @Autowired
    private WalletServiceImpl underTest;

    @Test
    void itShouldUpdateWalletName() {
        //GIVEN
        UpdateWalletDTO updateWalletDto = new UpdateWalletDTO(1L, "walletName");
        Wallet wallet = new Wallet("anyName");
        wallet.setId(1L);
        Instant time = Instant.now();
        wallet.setCreationDate(Instant.now());

        WalletDTO newWallet = new WalletDTO(1L,"walletName",time);
        given(walletRepository.findById(updateWalletDto.id()))
                .willReturn(Optional.of(wallet));
        given(mapper.mapWalletEntityToWalletDTO(Mockito.any(Wallet.class))).willReturn(newWallet);
        //WHEN
        WalletDTO walletDTO = underTest.updateWallet(updateWalletDto);
        //THEN
        assertThat(walletDTO.name()).isEqualTo(updateWalletDto.name());
    }

    @Test
    void itShouldThrowWhenWalletNotFound() {
        //GIVEN
        given(walletRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        UpdateWalletDTO updateWalletDto = new UpdateWalletDTO(1L, "walletName");
        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.updateWallet(updateWalletDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}