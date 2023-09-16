package pl.byczazagroda.trackexpensesappbackend.wallet;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.Wallet;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.dto.WalletDTO;

@Mapper(componentModel = "spring")
public interface WalletModelMapper {

    @Mapping(source = "user.id", target = "userId")
    WalletDTO mapWalletEntityToWalletDTO(Wallet wallet);
}


