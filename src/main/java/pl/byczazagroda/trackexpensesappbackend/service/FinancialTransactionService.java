package pl.byczazagroda.trackexpensesappbackend.service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public interface FinancialTransactionService {

    void deleteFinancialTransactionById(@Min(1) @NotNull Long id);
}
