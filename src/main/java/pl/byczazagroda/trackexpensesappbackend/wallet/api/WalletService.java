package pl.byczazagroda.trackexpensesappbackend.wallet.api;


import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletUpdateDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Validated
public interface WalletService {

    WalletDTO createWallet(@Valid WalletCreateDTO walletCreateDTO, Long userId);

    WalletDTO updateWallet(@Min(1) @NotNull Long id, @Valid WalletUpdateDTO walletToUpdate, Long userId);

    List<WalletDTO> getWallets(Long userId);

    void deleteWalletById(@Min(1) @NotNull Long walletId, Long userId);

    WalletDTO findById(@Min(1) @NotNull Long walletId, Long userId);

    List<WalletDTO> findAllByNameIgnoreCase(@NotBlank @Length(max = 20) @Pattern(regexp = "[\\w ]+") String name, Long userId);
}
