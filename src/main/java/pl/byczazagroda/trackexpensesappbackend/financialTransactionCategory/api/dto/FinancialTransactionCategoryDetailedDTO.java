package pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.api.dto;

import java.math.BigInteger;

public record FinancialTransactionCategoryDetailedDTO(FinancialTransactionCategoryDTO financialTransactionCategoryDTO,
                                                      BigInteger financialTransactionsCounter) {

}
