package pl.byczazagroda.trackexpensesappbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;

public record FinancialTransactionCreateDTO(@Min(1) @NotNull Long walletId,
                                            @Digits(integer = 13, fraction = 2) @PositiveOrZero BigDecimal amount,
                                            String description,
                                            @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
                                            Instant date,
                                            @NotNull FinancialTransactionType type, Long categoryId) {
}
