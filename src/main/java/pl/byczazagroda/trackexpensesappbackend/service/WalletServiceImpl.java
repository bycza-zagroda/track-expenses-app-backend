package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletModelMapper;
import pl.byczazagroda.trackexpensesappbackend.exception.WalletNotFoundException;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Primary
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletModelMapper mapper;

    //TODO: czy walidacja w serwisie też potrzebna? jeśli tak to co z @ExceptionHandler?
    //TODO: czy rzucanie tutaj wyjątku WalletNotFoundException jest OK? czy trzeba koniecznie do kontrolera?
    //TODO: czy mapowanie z użyciem MapStruct jest OK?
    @Transactional
    public WalletDTO updateWallet(@NotNull @Min(value = 1) long id,
                                  @NotNull @NotEmpty @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+") String name){
        Wallet wallet = walletRepository.findById(id).orElseThrow(() ->
        {
            throw new WalletNotFoundException(String.format("Wallet with given ID: %s does not exist", id));
        });

        wallet.setName(name);
        //TODO: czy potrzebujemy tutaj zapisywać encję?
        walletRepository.save(wallet);

        return mapper.toDto(wallet);
    }

}
