package pl.byczazagroda.trackexpensesappbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;

@Mapper(componentModel = "spring")
public interface FinancialTransactionCategoryModelMapper {

    @Mapping(source = "user.id", target = "userId")
    FinancialTransactionCategoryDTO mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(
            FinancialTransactionCategory category);

}
