package pl.byczazagroda.trackexpensesappbackend.mapper;


import org.mapstruct.Mapper;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;


@Mapper(componentModel = "spring")
public interface WalletModelMapper {
    WalletDTO mapWalletEntityToWalletDTO(Wallet wallet);
}


