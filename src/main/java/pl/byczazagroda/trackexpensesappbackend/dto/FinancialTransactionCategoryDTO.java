package pl.byczazagroda.trackexpensesappbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;

public record FinancialTransactionCategoryDTO(Long id, String name, FinancialTransactionType type,
                                              @JsonInclude(JsonInclude.Include.ALWAYS) Long userId){}
