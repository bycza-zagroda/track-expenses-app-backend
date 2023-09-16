package pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategory;

@Mapper(componentModel = "spring")
public interface FinancialTransactionCategoryModelMapper {

    @Mapping(source = "user.id", target = "userId")
    FinancialTransactionCategoryDTO mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(
            FinancialTransactionCategory category);

}
