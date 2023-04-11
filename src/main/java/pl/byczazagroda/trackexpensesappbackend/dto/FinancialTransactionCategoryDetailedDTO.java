package pl.byczazagroda.trackexpensesappbackend.dto;

import java.math.BigInteger;

public record FinancialTransactionCategoryDetailedDTO(FinancialTransactionCategoryDTO financialTransactionCategoryDTO,
                                                      BigInteger financialTransactionsCounter){}
