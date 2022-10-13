package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

import javax.validation.Valid;

public interface WalletService {
    WalletDTO createWallet(@Valid CreateWalletDTO createWalletDTO);
}
