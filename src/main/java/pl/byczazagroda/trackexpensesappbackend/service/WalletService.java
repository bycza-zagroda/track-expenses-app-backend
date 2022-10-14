package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

import javax.validation.Valid;
import java.util.List;

public interface WalletService {
    WalletDTO createWallet(@Valid CreateWalletDTO createWalletDTO);

    List<WalletDTO> getAllWallets();
}
