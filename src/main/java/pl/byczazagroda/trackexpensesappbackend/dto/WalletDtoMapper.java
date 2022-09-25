package pl.byczazagroda.trackexpensesappbackend.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WalletDtoMapper {

    WalletDTO toDto(Wallet wallet);
}
