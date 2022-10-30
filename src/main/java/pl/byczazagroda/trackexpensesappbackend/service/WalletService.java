package pl.byczazagroda.trackexpensesappbackend.service;


import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

import javax.validation.Valid;

public interface WalletService {

    WalletDTO createWallet(CreateWalletDTO createWalletDTO);

    WalletDTO findOne(@Valid Long id);

    WalletDTO updateWallet(UpdateWalletDTO walletToUpdate);
}
