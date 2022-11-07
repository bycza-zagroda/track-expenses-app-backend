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
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Controller for Wallet application.
 */
@RestController
@RequestMapping("api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PutMapping
    public ResponseEntity<WalletDTO> updateWallet(@Valid @RequestBody UpdateWalletDTO updateWalletDto) {

        WalletDTO walletDTO = walletService.updateWallet(updateWalletDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully updated Wallet!");
        return new ResponseEntity<>(walletDTO, headers, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<WalletDTO> createWallet(@Valid @RequestBody CreateWalletDTO createWalletDTO) {

        WalletDTO walletDTO = walletService.createWallet(createWalletDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully completed the creation of a new Wallet!");

        return new ResponseEntity<>(walletDTO, headers, HttpStatus.CREATED);
    }

    @GetMapping()
    ResponseEntity<List<WalletDTO>> getWallets() {

        List<WalletDTO> walletsDTO = walletService.getWallets();
        HttpHeaders headers =  new HttpHeaders();

        if (!walletsDTO.isEmpty()) {
            headers.add("message", "The list of wallets has been successfully retrieved.");
        } else {
            headers.add("message", "There are no available wallets to view.");
        }

        return new ResponseEntity<>(walletsDTO, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WalletDTO> deleteWalletById(@Valid @Min(1) @PathVariable Long id) {
        walletService.deleteWalletById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully completed the delete of a Wallet!");

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    ResponseEntity<List<WalletDTO>> getWalletsByName(@PathVariable String name) {
        List<WalletDTO> walletsDTO = walletService.getWalletsByName(name);
        HttpHeaders headers = new HttpHeaders();

        if (!walletsDTO.isEmpty()) {
            headers.add("message", "The list of wallets has been successfully retrieved.");
        } else {
            headers.add("message", "There are no available wallets to view.");
        }

        return new ResponseEntity<>(walletsDTO, headers, HttpStatus.OK);
    }
}
