package pl.byczazagroda.trackexpensesappbackend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;

    @Override
    public void deleteFinancialTransactionById(@Min(1) @NotNull Long id) {
        if (financialTransactionRepository.existsById(id)) {
            financialTransactionRepository.deleteById(id);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.I001,
                    String.format("FinancialTransaction with given id: %d does not exist", id));
        }
    }
}
