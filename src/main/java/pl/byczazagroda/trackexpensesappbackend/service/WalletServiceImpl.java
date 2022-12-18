package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateWalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ApiExceptionBase;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
    public WalletDTO createWallet(@Valid CreateWalletDTO createWalletDTO) {

            String walletName = createWalletDTO.name();
            Wallet wallet = new Wallet(walletName);
            Wallet savedWallet = walletRepository.save(wallet);
        return walletModelMapper.mapWalletEntityToWalletDTO(savedWallet);
    }

    @Override
    @Transactional
    public WalletDTO updateWallet(@Valid UpdateWalletDTO dto) {
        Wallet wallet = walletRepository.findById(dto.id())
                .orElseThrow(() -> {
                    throw new AppRuntimeException(
                            ErrorCode.W003,
                            String.format("Wallet with id: %d does not exist", dto.id()));
                });
        wallet.setName(dto.name());

        return walletModelMapper.mapWalletEntityToWalletDTO(wallet);
    }

    @Override
    public List<WalletDTO> getWallets() {
        return walletRepository.findAll().stream()
                .map(walletModelMapper::mapWalletEntityToWalletDTO)
                .toList();
    }

    @Override
    public void deleteWalletById(@Valid @NotNull
                                     @Min(value = 1, message = "Wallet id has to be greater than 0") Long id) {
        if (walletRepository.existsById(id)) {
            walletRepository.deleteById(id);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.W003,
                    String.format("Wallet with given id: %d does not exist", id));
        }
    }

    @Override
    public WalletDTO findById(@Valid @NotNull
                                  @Min(value = 1, message = "Wallet id has to be greater than 0") Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        return wallet.map(walletModelMapper::mapWalletEntityToWalletDTO).orElseThrow(()->new AppRuntimeException(ErrorCode.W003, String.format("Wallet with id: %s not found", id)));
    }

    @Override
    public List<WalletDTO> findAllByNameLikeIgnoreCase(@Valid @NotBlank() @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+") String name) {
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
