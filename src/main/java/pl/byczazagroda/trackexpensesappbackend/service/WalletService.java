package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

public interface WalletService {

    WalletDTO updateWallet(UpdateWalletDTO walletToUpdate);
}
