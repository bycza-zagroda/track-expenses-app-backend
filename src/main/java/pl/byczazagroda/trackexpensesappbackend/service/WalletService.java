package pl.byczazagroda.trackexpensesappbackend.service;


import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import java.util.List;

@Validated
public interface WalletService {

    WalletDTO updateWallet(@Valid UpdateWalletDTO walletToUpdate);

    WalletDTO createWallet(@Valid CreateWalletDTO createWalletDTO);

    List<WalletDTO> getWallets();

    void deleteWalletById(@Min(value = 1, message = "Wallet id has to be greater than 0")
                          Long id);

    List<WalletDTO> getWalletsByName(@Valid @NotBlank @NotEmpty @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+") String name);
}
