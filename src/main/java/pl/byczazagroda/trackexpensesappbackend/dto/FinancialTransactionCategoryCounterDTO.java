package pl.byczazagroda.trackexpensesappbackend.dto;

import java.math.BigInteger;

public record FinancialTransactionCategoryCounterDTO(FinancialTransactionCategoryDTO financialTransactionCategoryDTO,
                                                     BigInteger financialTransactionsCounter){}
