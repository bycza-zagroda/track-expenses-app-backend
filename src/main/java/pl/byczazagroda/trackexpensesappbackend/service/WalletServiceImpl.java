package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotDeletedException;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotSavedException;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static pl.byczazagroda.trackexpensesappbackend.exception.WalletExceptionMessages.WALLETS_LIST_NOT_FOUND_EXC_MSG;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletModelMapper walletModelMapper;

    @Override
    @Transactional
    public WalletDTO updateWallet(UpdateWalletDTO dto) throws ResourceNotFoundException {
        Wallet wallet = walletRepository.findById(dto.id()).orElseThrow(() -> {
            throw new ResourceNotFoundException(String.format("Wallet with given ID: %s does not exist", dto.id()));
        });
        wallet.setName(dto.name());

        return walletModelMapper.mapWalletEntityToWalletDTO(wallet);
    }

    @Override
    public WalletDTO createWallet(CreateWalletDTO createWalletDTO) {
        String walletName = createWalletDTO.name();
        Wallet wallet = new Wallet(walletName);
        Wallet savedWallet = walletRepository.save(wallet);
        boolean isWalletExists = walletRepository.existsById(savedWallet.getId());

        if (isWalletExists) {
            return walletModelMapper.mapWalletEntityToWalletDTO(savedWallet);
        }
        throw new ResourceNotSavedException("Sorry. Something went wrong and your Wallet was not saved. Please contact with administrator.");
    }

    @Override
    public List<WalletDTO> getWallets() {
        List<WalletDTO> walletsDTO;
        try {
            walletsDTO = walletRepository.findAll().stream()
                    .map(walletModelMapper::mapWalletEntityToWalletDTO)
                    .toList();
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException(WALLETS_LIST_NOT_FOUND_EXC_MSG);
        }
        return walletsDTO;
    }

    @Override
    public void deleteWalletById(Long id) {
        if (walletRepository.existsById(id)) {
            walletRepository.deleteById(id);
        } else {
            throw new ResourceNotDeletedException("Value does not exist in the database, please change your request");
        }
    }

    @Override
    public WalletDTO findById(Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()) {
            return walletModelMapper.mapWalletEntityToWalletDTO(wallet.get());
        } else {
            throw new ResourceNotFoundException("Wallet with that id doesn't exist");
        }
    }

    @Override
    public List<WalletDTO> getWalletsByName(String name) {
        return getWallets().stream().filter(walletDTO -> walletDTO.name().contains(name)).toList();
    }
}
