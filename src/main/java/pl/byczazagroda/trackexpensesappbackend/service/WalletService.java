package pl.byczazagroda.trackexpensesappbackend.service;


import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Validated
public interface WalletService {

    WalletDTO createWallet(@Valid CreateWalletDTO createWalletDTO);

    WalletDTO updateWallet(@Valid UpdateWalletDTO walletToUpdate);

    List<WalletDTO> getWallets();

    void deleteWalletById(@Valid @NotNull
                          @Min(value = 1, message = "Wallet id has to be greater than 0")
                          Long id);

    List<WalletDTO> findAllByNameLikeIgnoreCase(@Valid @NotBlank() @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+") String name);

    WalletDTO findById(@Valid @NotNull
                       @Min(value = 1, message = "Wallet id has to be greater than 0")
                       Long id);
}
