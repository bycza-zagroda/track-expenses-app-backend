package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.byczazagroda.trackexpensesappbackend.controller.WalletController;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@WebMvcTest
        (controllers = WalletController.class,
                includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        WalletRepository.class,
                        WalletServiceImpl.class}))
@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest2 {

    @InjectMocks
    private WalletServiceImpl sut;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletModelMapper walletModelMapper;

    @Test
    void createWallet() {
        //given
        String name = "Wallet3";
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(name);
        Wallet wallet = new Wallet(createWalletDTO.name());
        //when

        sut.createWallet(createWalletDTO);


//        Mockito.when( walletRepository.save(wallet)).thenThrow(MethodArgumentNotValidException.class);
        //then

//        assertThrows(MethodArgumentNotValidException.class, ()-> sut.createWallet(createWalletDTO));

        assertThatThrownBy(() -> sut.createWallet(createWalletDTO))
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessage("VALIDATION_FAILED");
    }


    @Test
    void createWallet2() {
        //given
        String name = "Wallet3";
        CreateWalletDTO createWalletDTO = new CreateWalletDTO(name);
        Wallet wallet = new Wallet(createWalletDTO.name());
        //when

        walletRepository.save(wallet);

      WalletDTO walletDTO = new WalletDTO(1L, name, Instant.now());
//        Mockito.when(walletModelMapper.mapWalletEntityToWalletDTO(wallet))
//                .thenReturn(walletDTO);
            sut.createWallet(createWalletDTO);
//        Mockito.when( walletRepository.save(wallet)).thenThrow(MethodArgumentNotValidException.class);
        //then

//        assertThrows(MethodArgumentNotValidException.class, ()-> sut.createWallet(createWalletDTO));
//        assertEquals(wallet.getName(), walletDTO.name());
        assertThatThrownBy(() -> sut.createWallet(createWalletDTO))
                .isInstanceOf(MethodArgumentNotValidException.class)
                .hasMessage("VALIDATION_FAILED");
    }
}