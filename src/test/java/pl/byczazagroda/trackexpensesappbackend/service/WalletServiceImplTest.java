package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private WalletModelMapper walletModelMapper;


    @Test
    void shouldCreateWalletProperly() {
//      given
        Instant creationTime = Instant.now();
        CreateWalletDTO createWalletDTO = new CreateWalletDTO("name");
        Wallet wallet = new Wallet("name");
        wallet.setId(1L);
        WalletDTO walletDTO = new WalletDTO(1L, "name", creationTime);

//        when
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletRepository.existsById(1L)).thenReturn(true);
        when(walletModelMapper.mapWalletEntityToWalletDTO(wallet)).thenReturn(walletDTO);
        WalletDTO returnedWallet = walletService.createWallet(createWalletDTO);

//        then
        Assertions.assertEquals(wallet.getId(), returnedWallet.id());
        Assertions.assertEquals(wallet.getName(), returnedWallet.name());
        Assertions.assertEquals(wallet.getCreationDate(), returnedWallet.creationDate());

    }
}