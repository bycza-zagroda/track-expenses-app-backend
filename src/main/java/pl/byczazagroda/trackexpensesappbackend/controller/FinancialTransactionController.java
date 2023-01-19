package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;


    @GetMapping()
    ResponseEntity<List<FinancialTransactionDTO>> getFinancialTransactionsByWalletId(@RequestParam @Min(1) @NotNull Long walletId) {
        List<FinancialTransactionDTO> financialTransactionDTOList = financialTransactionService.getFinancialTransactionsByWalletId(walletId);
        return new ResponseEntity<>(financialTransactionDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialTransactionDTO> findTransactionById(@Min(1) @NotNull @PathVariable Long id) {

        FinancialTransactionDTO financialTransaction = financialTransactionService.findById(id);
        return new ResponseEntity<>(financialTransaction, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionById(@Min(1) @NotNull @PathVariable Long id) {
        financialTransactionService.deleteTransactionById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
