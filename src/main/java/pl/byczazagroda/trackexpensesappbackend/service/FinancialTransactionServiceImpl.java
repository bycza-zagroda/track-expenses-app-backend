package pl.byczazagroda.trackexpensesappbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;

    private final FinancialTransactionModelMapper financialTransactionModelMapper;

    @Override
    public List<FinancialTransactionDTO> getFinancialTransactionsByWalletId(@Min(1) @NotNull Long walletId) {
        return financialTransactionRepository.findAllByWalletIdOrderByTransactionDateDesc(walletId).stream()
                .map(financialTransactionModelMapper::mapFinancialTransactionEntityToFinancialTransactionDTO)
                .toList();
    }

    @Override
    public FinancialTransactionDTO findById(@Min(1) @NotNull Long id) {
        Optional<FinancialTransaction> financialTransaction = financialTransactionRepository.findById(id);
        return financialTransaction.map(financialTransactionModelMapper::mapFinancialTransactionEntityToFinancialTransactionDTO)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FT01,
                        String.format("Financial transaction with id: %d not found", id)));
    }

    @Override
    public void deleteTransactionById(@Min(1) @NotNull Long id) {
        if (financialTransactionRepository.existsById(id)) {
            financialTransactionRepository.deleteById(id);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.FT01,
                    String.format("FinancialTransaction with given id: %d does not exist", id));
        }
    }
}
