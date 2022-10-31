package pl.byczazagroda.trackexpensesappbackend.service;


import org.hibernate.validator.constraints.NotBlank;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;

import javax.validation.Valid;
import java.util.List;

public interface WalletService {

    WalletDTO updateWallet(@Valid UpdateWalletDTO walletToUpdate);

    WalletDTO createWallet(@Valid CreateWalletDTO createWalletDTO);

    List<WalletDTO> getWallets();

    void deleteWalletById(@NotBlank(message = "Wallet id has to be present")
                          Long id);
}
