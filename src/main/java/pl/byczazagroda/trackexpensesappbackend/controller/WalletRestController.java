package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.model.dto.CreateWalletDto;
import pl.byczazagroda.trackexpensesappbackend.model.dto.WalletDto;
import pl.byczazagroda.trackexpensesappbackend.service.WalletServiceImpl;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
@RestController
@RequiredArgsConstructor
@Slf4j
class WalletRestController {

  @NonNull
  private final WalletServiceImpl walletService;

  @PostMapping("api/v1/wallets")
  public ResponseEntity<WalletDto> create(@Valid @RequestBody CreateWalletDto createWalletDto) {
    log.info("Creating wallet, CreateWalletDto={}", createWalletDto);

    WalletDto wallet = walletService.createWallet(createWalletDto);

    log.info("Create wallet request processed");
    return ResponseEntity.status(CREATED).body(wallet);
  }
}
