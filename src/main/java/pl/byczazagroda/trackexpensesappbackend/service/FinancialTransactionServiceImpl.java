package pl.byczazagroda.trackexpensesappbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;

    private final FinancialTransactionModelMapper financialTransactionModelMapper;

    @Override
    public List<FinancialTransactionDTO> getFinancialTransactionsByWalletId(Long walletId) {
        return financialTransactionRepository.findAllByWalletIdOrderByTransactionDateDesc(walletId).stream()
                .map(financialTransactionModelMapper::mapFinancialTransactionEntityToFinancialTransactionDTO)
                .toList();
    }
}
