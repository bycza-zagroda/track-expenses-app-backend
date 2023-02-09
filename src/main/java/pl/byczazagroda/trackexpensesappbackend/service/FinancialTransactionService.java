package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.CreateFinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateFinancialTransactionDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface FinancialTransactionService {
    FinancialTransactionDTO createFinancialTransaction(@Valid CreateFinancialTransactionDTO createFinancialTransactionDTO);

    List<FinancialTransactionDTO> getFinancialTransactionsByWalletId(@Min(1) @NotNull Long walletId);

    FinancialTransactionDTO findById(@Min(1) @NotNull Long id);

    FinancialTransactionDTO updateFinancialTransaction(@Min(1) @NotNull Long id, @Valid UpdateFinancialTransactionDTO updateFinancialTransactionDTO);

    void deleteTransactionById(@Min(1) @NotNull Long id);
}
