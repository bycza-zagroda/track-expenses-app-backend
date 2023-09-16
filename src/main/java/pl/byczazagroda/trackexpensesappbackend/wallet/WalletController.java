package pl.byczazagroda.trackexpensesappbackend.wallet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.dto.error.ErrorResponseDTO;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.WalletService;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletUpdateDTO;

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
public class WalletController {

    private final WalletService walletService;

    @PostMapping()
    public ResponseEntity<WalletDTO> createWallet(@Valid @RequestBody WalletCreateDTO walletCreateDTO,
                                                  Principal principal) {
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
    public ResponseEntity<WalletDTO> updateWallet(@Min(1) @NotNull @PathVariable Long id,
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
