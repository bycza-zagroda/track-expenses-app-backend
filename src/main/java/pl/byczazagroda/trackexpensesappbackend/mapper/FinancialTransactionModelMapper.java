package pl.byczazagroda.trackexpensesappbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;

@Mapper(componentModel = "spring")
public interface FinancialTransactionModelMapper {
    @Mapping(source = "financialTransactionCategory.id", target = "categoryId")
    FinancialTransactionDTO mapFinancialTransactionEntityToFinancialTransactionDTO(FinancialTransaction transaction);
}
