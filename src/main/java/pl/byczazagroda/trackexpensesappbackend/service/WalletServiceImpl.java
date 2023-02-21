package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletModelMapper walletModelMapper;

    @Override
    public WalletDTO createWallet(@Valid WalletCreateDTO walletCreateDTO) {

        String walletName = walletCreateDTO.name();
        Wallet wallet = new Wallet(walletName);
        Wallet savedWallet = walletRepository.save(wallet);
        return walletModelMapper.mapWalletEntityToWalletDTO(savedWallet);
    }

    @Override
    @Transactional
    public WalletDTO updateWallet(@Min(1) @NotNull Long id, @Valid WalletUpdateDTO dto) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> {
                    throw new AppRuntimeException(
                            ErrorCode.W003,
                            String.format("Wallet with id: %d does not exist", id));
                });
        wallet.setName(dto.name());

        return walletModelMapper.mapWalletEntityToWalletDTO(wallet);
    }

    @Override
    public List<WalletDTO> getWallets() {
        return walletRepository.findAllByOrderByNameAsc().stream()
                .map(walletModelMapper::mapWalletEntityToWalletDTO)
                .toList();
    }

    @Override
    public void deleteWalletById(@Min(1) @NotNull Long id) {
        if (walletRepository.existsById(id)) {
            walletRepository.deleteById(id);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.W003,
                    String.format("Wallet with given id: %d does not exist", id));
        }
    }

    @Override
    public WalletDTO findById(@Min(1) @NotNull Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        return wallet.map(walletModelMapper::mapWalletEntityToWalletDTO)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.W003,
                        String.format("Wallet with id: %s not found", id)));
    }

    @Override
    public List<WalletDTO> findAllByNameLikeIgnoreCase(@NotBlank() @Length(max = 20) @Pattern(regexp = "[\\w ]+") String name) {
        List<WalletDTO> listOfWalletDTO;
        try {
            listOfWalletDTO = walletRepository.findAllByNameLikeIgnoreCase(name)
                    .stream()
                    .map(walletModelMapper::mapWalletEntityToWalletDTO)
                    .toList();
        } catch (RuntimeException e) {
            throw new AppRuntimeException(
                    ErrorCode.W004,
                    String.format("WALLETS_LIST_LIKE_%s_NOT_FOUND_EXC_MS", name));
        }
        return listOfWalletDTO;
    }
}
