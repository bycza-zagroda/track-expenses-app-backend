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
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDetailedDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryCreateDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionCategoryUpdateDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/categories")
public class FinancialTransactionCategoryController{

    @GetMapping("/{id}")
    ResponseEntity<FinancialTransactionCategoryDetailedDTO> getFinancialTransactionCategoryById(@Min(1) @NotNull @PathVariable Long id){
        //TODO Necessary code implementation
        FinancialTransactionCategoryDetailedDTO financialTransactionCategoryDetailedDTO = null;
        return new ResponseEntity<>(financialTransactionCategoryDetailedDTO, HttpStatus.OK);
    }

    @GetMapping()
    ResponseEntity<List<FinancialTransactionCategoryDTO>> getFinancialTransactionCategories(){
        //TODO Necessary code implementation
        List<FinancialTransactionCategoryDTO> financialTransactionCategoryDTOList = null;
        return new ResponseEntity<>(financialTransactionCategoryDTOList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<FinancialTransactionCategoryDTO> createFinancialTransactionCategory(@Valid @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO){
        //TODO Necessary code implementation
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = null;
        return new ResponseEntity<>(financialTransactionCategoryDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FinancialTransactionCategoryDTO> updateFinancialTransactionCategory(@Min(1) @NotNull @PathVariable Long id,
                                                                                              @Valid @RequestBody FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO){
        //TODO Necessary code implementation
        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = null;
        return new ResponseEntity<>(financialTransactionCategoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialTransactionCategoryById(@Min(1) @NotNull @PathVariable Long id){
        //TODO Necessary code implementation
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
