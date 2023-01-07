package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionService;

@RestController
@RequiredArgsConstructor
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;

}
