package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

import javax.validation.Valid;

public interface WalletService {

    WalletDTO updateWallet(@Valid UpdateWalletDTO walletToUpdate);
}
