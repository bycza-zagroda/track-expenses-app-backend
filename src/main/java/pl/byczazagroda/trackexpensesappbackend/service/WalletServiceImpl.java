package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.exception.ResourceNotFoundException;
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
    public WalletDTO updateWallet(UpdateWalletDTO dto) throws ResourceNotFoundException {
        Wallet wallet = walletRepository.findById(dto.id()).orElseThrow(() ->
        {
            throw new ResourceNotFoundException(String.format("Wallet with given ID: %s does not exist", dto.id()));
        });

        wallet.setName(dto.name());

        return mapper.mapWalletEntityToWalletDTO(wallet);
    }

}
