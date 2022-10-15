package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;

import javax.validation.Valid;
import java.util.List;

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
}
