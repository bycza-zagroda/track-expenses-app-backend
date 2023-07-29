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
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDetailedDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionCategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
@Validated
public class FinancialTransactionCategoryController {

    private final FinancialTransactionCategoryService financialTransactionCategoryService;

    @GetMapping("/{id}")
    ResponseEntity<FinancialTransactionCategoryDetailedDTO> getFinancialTransactionCategoryById(
            @Min(1) @NotNull @PathVariable Long id,
            Principal principal) {

        Long userId = Long.valueOf(principal.getName());

        FinancialTransactionCategoryDetailedDTO financialTransactionCategoryDetailedDTO =
                financialTransactionCategoryService.findCategoryForUser(id, userId);

        return new ResponseEntity<>(financialTransactionCategoryDetailedDTO, HttpStatus.OK);
    }

    @GetMapping()
    ResponseEntity<List<FinancialTransactionCategoryDTO>> getFinancialTransactionCategories(Principal principal) {

        Long userId = Long.valueOf(principal.getName());

        List<FinancialTransactionCategoryDTO> financialTransactionCategoryDTOList
                = financialTransactionCategoryService.getFinancialTransactionCategories(userId);

        return new ResponseEntity<>(financialTransactionCategoryDTOList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<FinancialTransactionCategoryDTO> createFinancialTransactionCategory(
            @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO,
            Principal principal) {

        Long userId = Long.valueOf(principal.getName());

        FinancialTransactionCategoryDTO financialTransactionCategoryDTO =
                financialTransactionCategoryService.createFinancialTransactionCategory(financialTransactionCategoryCreateDTO, userId);

        return new ResponseEntity<>(financialTransactionCategoryDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FinancialTransactionCategoryDTO> updateFinancialTransactionCategory(
            @Min(1) @NotNull @PathVariable(name = "id") Long categoryId,
            @Valid @RequestBody FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO,
            Principal principal) {

        Long userId = Long.valueOf(principal.getName());

        FinancialTransactionCategoryDTO financialTransactionCategoryDTO
                = financialTransactionCategoryService.updateFinancialTransactionCategory(categoryId, userId, financialTransactionCategoryUpdateDTO);

        return new ResponseEntity<>(financialTransactionCategoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialTransactionCategoryById(@Min(1) @NotNull @PathVariable(name = "id")  Long categoryId, Principal principal) {

        Long userId = Long.valueOf(principal.getName());

        financialTransactionCategoryService.deleteFinancialTransactionCategory(categoryId, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
