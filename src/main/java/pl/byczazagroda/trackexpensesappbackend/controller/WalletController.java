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
import pl.byczazagroda.trackexpensesappbackend.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.error.ErrorResponseDTO;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.security.Principal;
import java.util.List;


/**
 * Controller for Wallet application.
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/wallets")
//fixme, new issue, required improve method for wallets
public class WalletController {

    private final WalletService walletService;

    @PostMapping()
    public ResponseEntity<WalletDTO> createWallet(
            @Valid @RequestBody WalletCreateDTO walletCreateDTO,
            Principal principal
    ) {
        Long userId = Long.valueOf(principal.getName());

        WalletDTO walletDTO = walletService.createWallet(walletCreateDTO, userId);

        return new ResponseEntity<>(walletDTO, HttpStatus.CREATED);
    }

    @Operation(
            responses = {@ApiResponse(
                    responseCode = "404",

                    description = "wallet not found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )})
            })

    @PatchMapping("/{id}")
    public ResponseEntity<WalletDTO> updateWallet(
            @Min(1) @NotNull @PathVariable Long id,
            @Valid @RequestBody WalletUpdateDTO walletUpdateDto,
            Principal principal
    ) {
        Long userId = Long.valueOf(principal.getName());
        WalletDTO walletDTO = walletService.updateWallet(id, walletUpdateDto, userId);
        return new ResponseEntity<>(walletDTO, HttpStatus.OK);
    }

    @GetMapping()
    ResponseEntity<List<WalletDTO>> getWallets(
            Principal principal
    ) {
        Long userId = Long.valueOf(principal.getName());
        List<WalletDTO> walletsDTO = walletService.getWallets(userId);
        return new ResponseEntity<>(walletsDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWalletById(@Min(1) @NotNull @PathVariable Long id, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        walletService.deleteWalletById(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDTO> findWalletById(@Min(1) @NotNull @PathVariable Long id, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        WalletDTO walletDTO = walletService.findById(id, userId);
        return new ResponseEntity<>(walletDTO, HttpStatus.OK);
    }

    @GetMapping("/wallets/{name}")
    ResponseEntity<List<WalletDTO>> findAllByNameLikeIgnoreCase(
            @PathVariable @NotBlank @Pattern(regexp = "[\\w ]+") @Length(max = 20)
            String name, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        List<WalletDTO> walletsDTO = walletService.findAllByNameIgnoreCase(name, userId);
        return new ResponseEntity<>(walletsDTO, HttpStatus.OK);
    }
}
