package pl.byczazagroda.trackexpensesappbackend.service;


import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

import javax.validation.Valid;

public interface WalletService {

    WalletDTO updateWallet(UpdateWalletDTO walletToUpdate);

    WalletDTO createWallet(CreateWalletDTO createWalletDTO);
}
