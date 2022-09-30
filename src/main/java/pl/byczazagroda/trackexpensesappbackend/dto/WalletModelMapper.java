package pl.byczazagroda.trackexpensesappbackend.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WalletModelMapper {

    WalletDTO mapWalletEntityToWalletDTO(Wallet wallet);}
