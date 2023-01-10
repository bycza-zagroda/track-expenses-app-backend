package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;

import java.util.List;

public interface FinancialTransactionService {

    List<FinancialTransactionDTO> getFinancialTransactionsByWalletId(Long walletId);
}
