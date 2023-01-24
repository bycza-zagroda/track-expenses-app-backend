package pl.byczazagroda.trackexpensesappbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateFinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
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
    @Transactional
    public FinancialTransactionDTO updateTransaction(
            @Min(1) @NotNull Long id,
            @Valid UpdateFinancialTransactionDTO updateTransactionDTO){

        FinancialTransaction financialTransaction = financialTransactionRepository.findById(id)
                .orElseThrow(()-> {
                    throw new AppRuntimeException(ErrorCode.FT01,
                            String.format("Financial transaction with id: %d not found", id));
                });

        financialTransaction.builder()
                .amount(updateTransactionDTO.amount())
                .description(updateTransactionDTO.description())
                .transactionDate(Instant.now())
                .build();

        return financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction);
    }
}
