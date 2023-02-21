package pl.byczazagroda.trackexpensesappbackend.service;


import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Validated
public interface WalletService {

    WalletDTO createWallet(@Valid WalletCreateDTO walletCreateDTO);

    WalletDTO updateWallet(@Min(1) @NotNull Long id, @Valid WalletUpdateDTO walletToUpdate);

    List<WalletDTO> getWallets();

    void deleteWalletById(@Min(1) @NotNull Long id);

    WalletDTO findById(@Min(1) @NotNull Long id);

    List<WalletDTO> findAllByNameLikeIgnoreCase(@NotBlank @Length(max = 20) @Pattern(regexp = "[\\w ]+") String name);
}
