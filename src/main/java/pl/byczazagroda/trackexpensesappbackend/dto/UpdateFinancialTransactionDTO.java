package pl.byczazagroda.trackexpensesappbackend.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

public record UpdateFinancialTransactionDTO(
        @Positive BigDecimal amount,
        @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss") Instant transactionDate,
        @Size(max = 255) String description) {
}
