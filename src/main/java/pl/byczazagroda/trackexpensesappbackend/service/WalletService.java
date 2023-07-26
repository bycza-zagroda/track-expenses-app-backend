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

    WalletDTO createWallet(@Valid WalletCreateDTO walletCreateDTO, Long userId);

    WalletDTO updateWallet(@Min(1) @NotNull Long id, @Valid WalletUpdateDTO walletToUpdate, Long userId);

    List<WalletDTO> getWallets(Long userId);

    void deleteWalletById(@Min(1) @NotNull Long id, Long userId);

    WalletDTO findById(@Min(1) @NotNull Long id, Long userId);

    List<WalletDTO> findAllByNameIgnoreCase(@NotBlank @Length(max = 20) @Pattern(regexp = "[\\w ]+") String name, Long userId);
}
