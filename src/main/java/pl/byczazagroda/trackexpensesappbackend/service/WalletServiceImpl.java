package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.model.dto.CreateWalletDto;
import pl.byczazagroda.trackexpensesappbackend.model.dto.WalletDto;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;

  @Override
  public WalletDto createWallet(CreateWalletDto createWalletDto) {
    return WalletDto.builder()
        .build();
  }
}
