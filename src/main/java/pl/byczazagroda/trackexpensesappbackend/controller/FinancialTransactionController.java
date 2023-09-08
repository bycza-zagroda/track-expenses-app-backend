package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;


    @GetMapping()
    ResponseEntity<List<FinancialTransactionDTO>> getFinancialTransactionsByWalletId(
            @RequestParam @Min(1) @NotNull Long walletId, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        List<FinancialTransactionDTO> financialTransactionDTOList =
                financialTransactionService.getFinancialTransactionsByWalletId(walletId, userId);

        return new ResponseEntity<>(financialTransactionDTOList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<FinancialTransactionDTO> createFinancialTransaction(
            @Valid @RequestBody FinancialTransactionCreateDTO financialTransactionCreateDTO, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        FinancialTransactionDTO financialTransactionDTO =
                financialTransactionService.createFinancialTransaction(financialTransactionCreateDTO, userId);

        return new ResponseEntity<>(financialTransactionDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialTransactionDTO> findTransactionById(@Min(1) @NotNull @PathVariable Long id,
                                                                       Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        FinancialTransactionDTO financialTransaction = financialTransactionService
                .findFinancialTransactionForUser(id, userId);

        return new ResponseEntity<>(financialTransaction, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionById(@Min(1) @NotNull @PathVariable Long id, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        financialTransactionService.deleteTransactionById(id, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FinancialTransactionDTO> updateTransactionById(
            @Min(1) @NotNull @PathVariable Long id,
            @Valid @RequestBody FinancialTransactionUpdateDTO financialTransactionUpdateDTO, Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        FinancialTransactionDTO financialTransactionDTO =
                financialTransactionService.updateFinancialTransaction(id, financialTransactionUpdateDTO, userId);

        return new ResponseEntity<>(financialTransactionDTO, HttpStatus.OK);
    }
}
