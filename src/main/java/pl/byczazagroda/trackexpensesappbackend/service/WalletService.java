package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.model.dto.CreateWalletDto;
import pl.byczazagroda.trackexpensesappbackend.model.dto.WalletDto;

public interface WalletService {

  WalletDto createWallet(CreateWalletDto createWalletDto);
}
