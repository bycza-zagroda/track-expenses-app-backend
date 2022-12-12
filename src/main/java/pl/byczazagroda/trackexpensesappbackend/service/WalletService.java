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

    WalletDTO update(@Valid UpdateWalletDTO walletToUpdate);

    WalletDTO create(@Valid CreateWalletDTO createWalletDTO);

    List<WalletDTO> getAll();

    void deleteById(@NotNull @Min(value = 1, message = "Wallet id has to be greater than 0") Long id);

    WalletDTO findById(@NotNull @Min(value = 1, message = "Wallet id has to be greater than 0") Long id);

    List<WalletDTO> findAllByNameLikeIgnoreCase(@NotBlank @NotEmpty @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+") String name);
}
