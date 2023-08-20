package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


@Slf4j
@Service
@Validated
@RequiredArgsConstructor
//fixme, new issue, required improve method for wallets
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletModelMapper walletModelMapper;
    private final UserRepository userRepository;

    @Override
    public WalletDTO createWallet(@Valid WalletCreateDTO walletCreateDTO, Long userId) {
        String walletName = walletCreateDTO.name();
        User walletOwner = getUserByUserId(userId);

        Wallet wallet = new Wallet(walletName, walletOwner);

        Wallet savedWallet = walletRepository.save(wallet);
        return walletModelMapper.mapWalletEntityToWalletDTO(savedWallet);
    }

    @Override
    @Transactional
    public WalletDTO updateWallet(@Min(1) @NotNull Long id, @Valid WalletUpdateDTO dto, Long userId) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> {
                    throw new AppRuntimeException(
                            ErrorCode.W003,
                            String.format("Wallet with id: %d does not exist", id));
                });
        if (!wallet.getUser().getId().equals(userId)) {
            throw new AppRuntimeException(
                    ErrorCode.W005,
                    "You don't have permissions to update that wallet"
            );
        }

        wallet.setName(dto.name());

        return walletModelMapper.mapWalletEntityToWalletDTO(wallet);
    }

    @Override
    public List<WalletDTO> getWallets(Long userId) {
        return walletRepository.findAllByUserIdOrderByNameAsc(userId).stream()
                .map(walletModelMapper::mapWalletEntityToWalletDTO)
                .toList();
    }

    @Override
    public void deleteWalletById(@Min(1) @NotNull Long walletId, Long userId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> {
                    throw new AppRuntimeException(
                            ErrorCode.W003,
                            String.format("Wallet with id: %d does not exist", walletId));
                });
        if (!wallet.getUser().getId().equals(userId)) {
            throw new AppRuntimeException(
                    ErrorCode.W005,
                    "You don't have permissions to delete that wallet"
            );
        }
        walletRepository.deleteById(walletId);
    }

    @Override
    public WalletDTO findById(@Min(1) @NotNull Long walletId, Long userId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> {
                    throw new AppRuntimeException(
                            ErrorCode.W003,
                            String.format("Wallet with id: %d does not exist", walletId));
                });
        if (!wallet.getUser().getId().equals(userId)) {
            throw new AppRuntimeException(
                    ErrorCode.W005,
                    "You don't have permissions to view that wallet"
            );
        }
        return walletModelMapper.mapWalletEntityToWalletDTO(wallet);
    }

    @Override
    public List<WalletDTO> findAllByNameIgnoreCase(@NotBlank() @Length(max = 20) @Pattern(regexp = "[\\w ]+") String name, Long userId) {
        List<WalletDTO> listOfWalletDTO;
        try {
            listOfWalletDTO = walletRepository.findAllByUserIdAndNameIsContainingIgnoreCase(userId, name)
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

    private User getUserByUserId(Long userId) {
        return  userRepository.findById(userId).orElseThrow(() ->
                new AppRuntimeException(ErrorCode.U005, String.format("User with id: %d doesn't exist.", userId)));
    }

}
