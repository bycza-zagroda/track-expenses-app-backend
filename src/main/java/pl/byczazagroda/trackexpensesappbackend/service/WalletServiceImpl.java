package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;
import java.util.logging.Logger;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletModelMapper walletModelMapper;

    @Override
    public WalletDTO createWallet( CreateWalletDTO createWalletDTO) {
        String walletName = createWalletDTO.name();
        Wallet wallet = new Wallet(walletName);
        Wallet savedWallet = walletRepository.save(wallet);

//       if(walletRepository.existsById(savedWallet.getId())) { debug tutaj i tak nie chwodzi
           return walletModelMapper.mapWalletEntityToWalletDTO(savedWallet);
//       }

//       throw new ResourceNotFoundException("Wallet was not saved", savedWallet);
    }

    @Override
    public WalletDTO findOne( Long id) {
      return  walletRepository
                .findById(id)
                .map(walletModelMapper::mapWalletEntityToWalletDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Wallet with id: %s not found", id), id));
    }
    @Override
    @Transactional
    public WalletDTO updateWallet( UpdateWalletDTO dto) {
        Wallet wallet = walletRepository.findById(dto.id())
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Wallet with given ID: {} does not exist "+ dto.id());
                });
        wallet.setName(dto.name());

        return walletModelMapper.mapWalletEntityToWalletDTO(wallet);
    }
}
