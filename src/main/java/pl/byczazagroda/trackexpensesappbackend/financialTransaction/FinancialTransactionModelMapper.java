package pl.byczazagroda.trackexpensesappbackend.financialTransaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.FinancialTransaction;

@Mapper(componentModel = "spring")
public interface FinancialTransactionModelMapper {
    @Mapping(source = "financialTransactionCategory.id", target = "categoryId")
    FinancialTransactionDTO mapFinancialTransactionEntityToFinancialTransactionDTO(FinancialTransaction transaction);
}
