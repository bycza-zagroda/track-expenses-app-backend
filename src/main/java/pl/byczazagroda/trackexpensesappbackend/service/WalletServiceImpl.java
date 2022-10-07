package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.exception.WalletNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.transaction.Transactional;

@Primary
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletModelMapper mapper;

    @Override
    @Transactional
    public WalletDTO updateWallet(UpdateWalletDTO walletToUpdate) throws WalletNotFoundException {
        Wallet wallet = walletRepository.findById(walletToUpdate.id()).orElseThrow(() ->
        {
            throw new WalletNotFoundException(String.format("Wallet with given ID: %s does not exist", walletToUpdate.id()));
        });

        wallet.setName(walletToUpdate.name());

        return mapper.mapWalletEntityToWalletDTO(wallet);
    }

}
