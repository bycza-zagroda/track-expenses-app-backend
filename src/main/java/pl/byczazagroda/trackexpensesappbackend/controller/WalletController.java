package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;

import javax.validation.Valid;

/**
 * Controller for Wallet application.
 */
@RestController
@RequestMapping("api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;


    @PostMapping()
    public ResponseEntity<WalletDTO> createWallet(@Valid @RequestBody CreateWalletDTO createWalletDTO) {

        WalletDTO walletDTO = walletService.createWallet(createWalletDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully completed the creation of a new Wallet!");

        return new ResponseEntity<>(walletDTO, headers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDTO> findOne(@Valid @PathVariable Long id) {

        WalletDTO one = walletService.findOne(id);

        return new ResponseEntity<>(one, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<WalletDTO> updateWallet(@Valid @RequestBody UpdateWalletDTO updateWalletDto) {

        WalletDTO walletDTO = walletService.updateWallet(updateWalletDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully updated Wallet!");
        return new ResponseEntity<>(walletDTO, headers, HttpStatus.OK);
    }


}
