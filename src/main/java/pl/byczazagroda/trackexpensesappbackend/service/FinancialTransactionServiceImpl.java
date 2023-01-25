package pl.byczazagroda.trackexpensesappbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateFinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateFinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;
import pl.byczazagroda.trackexpensesappbackend.repository.WalletRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Validated
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
        FinancialTransaction financialTransaction = FinancialTransaction.builder()
                .financialTransactionType(createFinancialTransactionDTO.financialTransactionType())
                .transactionDate(createFinancialTransactionDTO.transactionDate())
                .description(createFinancialTransactionDTO.description())
                .wallet(wallet)
                .amount(createFinancialTransactionDTO.amount())
                .build();

        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction);

        return financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(savedFinancialTransaction);
    }

    @Override
    public List<FinancialTransactionDTO> getFinancialTransactionsByWalletId(@Min(1) @NotNull Long walletId) {
        List<FinancialTransaction> financialTransactionsList = financialTransactionRepository
                .findAllByWalletIdOrderByTransactionDateDesc(walletId);

        return financialTransactionsList.isEmpty() ? Collections.emptyList() : financialTransactionsList.stream()
                .map(financialTransactionModelMapper::mapFinancialTransactionEntityToFinancialTransactionDTO).toList();

    }

    @Override
    public FinancialTransactionDTO findById(@Min(1) @NotNull Long id) {
        FinancialTransaction financialTransaction = financialTransactionRepository.findById(id)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FT001,
                String.format("Financial transaction with id: %d not found", id)));

        return financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction);
    }

    @Override
    public void deleteTransactionById(@Min(1) @NotNull Long id) {
        if (financialTransactionRepository.existsById(id)) {
            financialTransactionRepository.deleteById(id);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.FT001,
                    String.format("FinancialTransaction with given id: %d does not exist", id));
        }
    }

    @Override
    @Transactional
    public FinancialTransactionDTO updateTransaction(
            @Min(1) @NotNull Long id,
            @Valid UpdateFinancialTransactionDTO updateTransactionDTO) {

        FinancialTransaction financialTransaction = financialTransactionRepository.findById(id)
                .orElseThrow(() -> {
                    throw new AppRuntimeException(ErrorCode.FT001,
                            String.format("Financial transaction with id: %d not found", id));
                });

        FinancialTransaction.builder()
                .amount(updateTransactionDTO.amount())
                .description(updateTransactionDTO.description())
                .transactionDate(Instant.now())
                .build();

        return financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction);
    }
}
