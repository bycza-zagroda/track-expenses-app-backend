package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.dto.CreateFinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/transactions/")
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;

    @PostMapping()
    public ResponseEntity<FinancialTransactionDTO> createFinancialTransaction(
            @Valid @RequestBody CreateFinancialTransactionDTO createFinancialTransactionDTO) {
        FinancialTransactionDTO financialTransactionDTO = financialTransactionService.createFinancialTransaction(createFinancialTransactionDTO);
        return new ResponseEntity<>(financialTransactionDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialTransactionDTO> findTransactionById(@Min(1) @NotNull @PathVariable Long id) {
        FinancialTransactionDTO financialTransaction = financialTransactionService.findById(id);
        return new ResponseEntity<>(financialTransaction, HttpStatus.OK);

    }
}
