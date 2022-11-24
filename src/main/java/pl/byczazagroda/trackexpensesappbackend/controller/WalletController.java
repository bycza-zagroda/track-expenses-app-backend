package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Controller for Wallet application.
 */
@RestController
@RequestMapping("api/wallet")
@RequiredArgsConstructor
@Validated
public class WalletController {

    private final WalletService walletService;

    @PutMapping
    public ResponseEntity<WalletDTO> update(@Valid @RequestBody UpdateWalletDTO updateWalletDto) {

        WalletDTO walletDTO = walletService.update(updateWalletDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully updated Wallet!");
        return new ResponseEntity<>(walletDTO, headers, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<WalletDTO> create(@Valid @RequestBody CreateWalletDTO createWalletDTO) {

        WalletDTO walletDTO = walletService.create(createWalletDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully completed the creation of a new Wallet!");

        return new ResponseEntity<>(walletDTO, headers, HttpStatus.CREATED);
    }

    @GetMapping()
    ResponseEntity<List<WalletDTO>> getAll() {

        List<WalletDTO> walletsDTO = walletService.getAll();
        HttpHeaders headers = new HttpHeaders();

        if (walletsDTO.isEmpty()) {
            headers.add("message", "There are no available wallets to view.");
        } else {
            headers.add("message", "The list of wallets has been successfully retrieved.");
        }

        return new ResponseEntity<>(walletsDTO, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WalletDTO> deleteById(@NotNull @Min(1) @PathVariable Long id) {
        walletService.deleteById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully completed the delete of a Wallet!");

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDTO> findById(@NotNull @Min(1) @PathVariable Long id) {
        WalletDTO walletDTO = walletService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The wallet has been successfully retrieved.");

        return new ResponseEntity<>(walletDTO, headers, HttpStatus.OK);
    }

    @GetMapping("/list/{name}")
    ResponseEntity<List<WalletDTO>> findByName(@PathVariable String name) {
        List<WalletDTO> walletsDTO = walletService.findAllByNameLikeIgnoreCase(name);
        HttpHeaders headers = new HttpHeaders();

        if (walletsDTO.isEmpty()) {
            headers.add("message", "There are no available wallets to view.");
        } else {
            headers.add("message", "The list of wallets has been successfully retrieved.");
        }

        return new ResponseEntity<>(walletsDTO, headers, HttpStatus.OK);
    }

}
