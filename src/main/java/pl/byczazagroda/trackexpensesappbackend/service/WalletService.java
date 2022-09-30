package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.EditWalletDto;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

public interface WalletService {

    WalletDTO updateWallet(EditWalletDto walletToEdit);
}
