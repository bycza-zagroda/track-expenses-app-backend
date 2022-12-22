package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/financial-transaction")
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFinancialTransactionById(@Min(1) @NotNull @PathVariable Long id) {

        financialTransactionService.deleteFinancialTransactionById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
