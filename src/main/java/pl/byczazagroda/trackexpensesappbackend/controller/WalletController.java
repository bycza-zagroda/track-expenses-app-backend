package pl.byczazagroda.trackexpensesappbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ApiException;
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

    @PostMapping()
    public ResponseEntity<WalletDTO> createWallet(
            @Valid @RequestBody CreateWalletDTO createWalletDTO) {

        WalletDTO walletDTO = walletService.createWallet(createWalletDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully completed the creation of a new Wallet!");

        return new ResponseEntity<>(walletDTO, headers, HttpStatus.CREATED);
    }

    @Operation(
            responses = {@ApiResponse(
                    responseCode = "404",

                    description = "wallet not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiException.class)
                    )})
            })
    @GetMapping("/map/{id}")
    public ResponseEntity<WalletDTO> findOne(@Valid @PathVariable Long id) {

        WalletDTO one = walletService.findOne(id);

        return new ResponseEntity<>(one, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<WalletDTO> updateWallet(
            @Valid @RequestBody UpdateWalletDTO updateWalletDto) {

        WalletDTO walletDTO = walletService.updateWallet(updateWalletDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully updated Wallet!");
        return new ResponseEntity<>(walletDTO, headers, HttpStatus.OK);
    }

    @GetMapping()
    ResponseEntity<List<WalletDTO>> getWallets() {

        List<WalletDTO> walletsDTO = walletService.getWallets();
        HttpHeaders headers = new HttpHeaders();

        if (walletsDTO.isEmpty()) {
            headers.add("message", "There are no available wallets to view.");
        } else {
            headers.add("message", "The list of wallets has been successfully retrieved.");
        }

        return new ResponseEntity<>(walletsDTO, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WalletDTO> deleteWalletById(@NotNull @Min(1) @PathVariable Long id) {
        walletService.deleteWalletById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "You have successfully completed the delete of a Wallet!");

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDTO> findWalletById(@NotNull @Min(1) @PathVariable Long id) {
        WalletDTO walletDTO = walletService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("message", "The wallet has been successfully retrieved.");

        return new ResponseEntity<>(walletDTO, headers, HttpStatus.OK);
    }
}
