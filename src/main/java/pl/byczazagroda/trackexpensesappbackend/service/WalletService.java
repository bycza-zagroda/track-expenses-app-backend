package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

public interface WalletService {
    WalletDTO createWallet(CreateWalletDTO createWalletDTO);
}
