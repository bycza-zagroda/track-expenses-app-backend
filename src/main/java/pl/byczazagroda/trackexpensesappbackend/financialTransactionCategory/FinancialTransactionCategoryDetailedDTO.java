package pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory;

import java.math.BigInteger;

public record FinancialTransactionCategoryDetailedDTO(FinancialTransactionCategoryDTO financialTransactionCategoryDTO,
                                                      BigInteger financialTransactionsCounter){}
