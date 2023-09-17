package pl.byczazagroda.trackexpensesappbackend.financialTransaction.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.api.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.api.model.FinancialTransaction;

@Mapper(componentModel = "spring")
public interface FinancialTransactionModelMapper {
    @Mapping(source = "financialTransactionCategory.id", target = "categoryId")
    FinancialTransactionDTO mapFinancialTransactionEntityToFinancialTransactionDTO(FinancialTransaction transaction);
}
