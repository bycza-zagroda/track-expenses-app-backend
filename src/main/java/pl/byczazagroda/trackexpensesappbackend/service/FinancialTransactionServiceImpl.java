package pl.byczazagroda.trackexpensesappbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateFinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final FinancialTransactionModelMapper financialTransactionModelMapper;
    private final WalletRepository walletRepository;

    @Override
    public FinancialTransactionDTO createFinancialTransaction(@Valid CreateFinancialTransactionDTO createFinancialTransactionDTO) {
        Long walletId = createFinancialTransactionDTO.walletId();
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> {
            throw new AppRuntimeException(ErrorCode.W003, String.format("Wallet with id: %d does not exist", walletId));
        });
        FinancialTransaction financialTransaction = FinancialTransaction.builder().financialTransactionType(createFinancialTransactionDTO.financialTransactionType()).transactionDate(Instant.now()).description(createFinancialTransactionDTO.description()).wallet(wallet).amount(createFinancialTransactionDTO.amount()).build();

        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction);
        return financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(savedFinancialTransaction);
    }

    @Override
    public List<FinancialTransactionDTO> getFinancialTransactionsByWalletId(@Min(1) @NotNull Long walletId) {
        List<FinancialTransaction> financialTransactionsList = financialTransactionRepository.findAllByWalletIdOrderByTransactionDateDesc(walletId);

        if (financialTransactionsList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return financialTransactionsList.stream().map(financialTransactionModelMapper::mapFinancialTransactionEntityToFinancialTransactionDTO).toList();
        }
    }

    @Override
    public FinancialTransactionDTO findById(@Min(1) @NotNull Long id) {
        Optional<FinancialTransaction> financialTransaction = financialTransactionRepository.findById(id);
        return financialTransaction.map(financialTransactionModelMapper::mapFinancialTransactionEntityToFinancialTransactionDTO).orElseThrow(() -> new AppRuntimeException(ErrorCode.FT01, String.format("Financial transaction with id: %d not found", id)));
    }
}
