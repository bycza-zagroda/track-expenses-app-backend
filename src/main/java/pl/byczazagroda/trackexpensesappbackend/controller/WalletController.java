package pl.byczazagroda.trackexpensesappbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


/**
 * Controller for Wallet application.
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/wallets")
public class WalletController {

    private final WalletService walletService;

    @PostMapping()
    public ResponseEntity<WalletDTO> createWallet(
            @Valid @RequestBody CreateWalletDTO createWalletDTO) {
        WalletDTO walletDTO = walletService.createWallet(createWalletDTO);

        return new ResponseEntity<>(walletDTO, HttpStatus.CREATED);
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

    @PatchMapping("/{id}")
    public ResponseEntity<WalletDTO> updateWallet(
            @Min(1) @NotNull @PathVariable Long id,
            @Valid @RequestBody UpdateWalletDTO updateWalletDto) {

        WalletDTO walletDTO = walletService.updateWallet(id, updateWalletDto);
        return new ResponseEntity<>(walletDTO, HttpStatus.OK);
    }

    @GetMapping()
    ResponseEntity<List<WalletDTO>> getWallets() {
        List<WalletDTO> walletsDTO = walletService.getWallets();
        return new ResponseEntity<>(walletsDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WalletDTO> deleteWalletById(@Min(1) @NotNull @PathVariable Long id) {

        walletService.deleteWalletById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDTO> findWalletById(@Min(1) @NotNull @PathVariable Long id) {

        WalletDTO walletDTO = walletService.findById(id);
        return new ResponseEntity<>(walletDTO, HttpStatus.OK);
    }

    @GetMapping("/wallets/{name}")
    ResponseEntity<List<WalletDTO>> findAllByNameLikeIgnoreCase(
            @PathVariable @NotBlank @Pattern(regexp = "[\\w ]+") @Length(max = 20)
            String name) {

        List<WalletDTO> walletsDTO = walletService.findAllByNameLikeIgnoreCase(name);
        return new ResponseEntity<>(walletsDTO, HttpStatus.OK);
    }
}
