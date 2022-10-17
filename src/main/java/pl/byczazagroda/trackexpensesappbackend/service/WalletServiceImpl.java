package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotSavedException;
import pl.byczazagroda.trackexpensesappbackend.exception.WalletNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import java.util.Optional;
@Service
@Validated
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletModelMapper walletModelMapper;


    @Override
    public WalletDTO createWallet(CreateWalletDTO createWalletDTO) {
        String walletName = createWalletDTO.name();
        Wallet wallet = new Wallet(walletName);
        Wallet savedWallet = walletRepository.save(wallet);
        boolean isWalletExists = walletRepository.existsById(savedWallet.getId());

        if (isWalletExists) {
            return walletModelMapper.mapWalletEntityToWalletDTO(savedWallet);
        }
        throw new ResourceNotSavedException("Sorry. Something went wrong and your Wallet is not saved. Contact administrator.");
    }

    @Override
    public WalletDTO findById(Long id) {
        Optional<Wallet> foundedWallet = walletRepository.findById(id);
        if (foundedWallet.isPresent()) {
            return walletModelMapper.mapWalletEntityToWalletDTO(foundedWallet.get());
        }
        throw new WalletNotFoundException("Wallet with that id doesn't exist");
    }
}
