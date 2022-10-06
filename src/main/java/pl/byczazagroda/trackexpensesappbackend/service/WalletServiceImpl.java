package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.WalletNotSavedException;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.validation.Valid;
import java.time.Instant;

@Service
@Validated
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletModelMapper walletModelMapper;


    @Override
    public WalletDTO createWallet(@Valid CreateWalletDTO createWalletDTO) {
        String walletName = createWalletDTO.name();
        Instant creationTime = createWalletDTO.creationTime();
        Wallet wallet = new Wallet(walletName, creationTime);
        Wallet savedWallet = walletRepository.save(wallet);
        boolean isWalletExists = walletRepository.existsById(savedWallet.getId());

        if (isWalletExists) {
            return walletModelMapper.mapWalletEntityToWalletDTO(savedWallet);
        }
        throw new WalletNotSavedException();
    }
}
