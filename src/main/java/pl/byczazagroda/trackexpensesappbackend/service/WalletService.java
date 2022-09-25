package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

public interface WalletService {

    WalletDTO updateWallet(long id, String name);
}
