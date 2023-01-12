package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateFinancialTransactionDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public interface FinancialTransactionService {
    FinancialTransactionDTO findById(@Min(1) @NotNull Long id);

    FinancialTransactionDTO updateTransaction(@Min(1) @NotNull Long id, @Valid UpdateFinancialTransactionDTO updateTransactionDTO);
}
