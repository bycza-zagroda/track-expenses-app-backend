package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.UpdateFinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/transactions/")
@Validated
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;

    @GetMapping("/{id}")
    public ResponseEntity<FinancialTransactionDTO> findTransactionById(@Min(1) @NotNull @PathVariable Long id) {

        FinancialTransactionDTO financialTransaction = financialTransactionService.findById(id);
        return new ResponseEntity<>(financialTransaction, HttpStatus.OK);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<FinancialTransactionDTO> updateTransactionById(
            @Min(1) @NotNull @PathVariable Long id,
            @Valid @RequestBody UpdateFinancialTransactionDTO updateTransactionDTO) {

        FinancialTransactionDTO financialTransactionDTO = financialTransactionService.updateTransaction(id, updateTransactionDTO);
        return new ResponseEntity<>(financialTransactionDTO, HttpStatus.OK);
    }
}
