package pl.byczazagroda.trackexpensesappbackend.mapper;

import org.mapstruct.Mapper;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;

@Mapper(componentModel = "spring")
public interface FinancialTransactionCategoryModelMapper {
    FinancialTransactionCategoryDTO mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(FinancialTransactionCategory category);
}
