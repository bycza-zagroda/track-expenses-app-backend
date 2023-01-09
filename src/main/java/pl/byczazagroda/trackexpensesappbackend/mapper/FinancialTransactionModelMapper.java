package pl.byczazagroda.trackexpensesappbackend.mapper;

import org.mapstruct.Mapper;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;

@Mapper(componentModel = "spring")
public interface FinancialTransactionModelMapper {

    FinancialTransactionDTO mapFinancialTransactionEntityToFinancialTransactionDTO(FinancialTransaction transaction);
}

