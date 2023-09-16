package pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api.model.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api.dto.FinancialTransactionCategoryDTO;

@Mapper(componentModel = "spring")
public interface FinancialTransactionCategoryModelMapper {

    @Mapping(source = "user.id", target = "userId")
    FinancialTransactionCategoryDTO mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(
            FinancialTransactionCategory category);

}
