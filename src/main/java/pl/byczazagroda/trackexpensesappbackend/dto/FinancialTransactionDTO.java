package pl.byczazagroda.trackexpensesappbackend.dto;

import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record FinancialTransactionDTO(Long id, BigDecimal amount, String description,
                                      FinancialTransactionType type, Instant date) {
}
