package pl.byczazagroda.trackexpensesappbackend.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;

@Mapper(componentModel = "spring")
public interface WalletModelMapper {

    WalletDTO mapWalletEntityToWalletDTO(Wallet wallet);
}
