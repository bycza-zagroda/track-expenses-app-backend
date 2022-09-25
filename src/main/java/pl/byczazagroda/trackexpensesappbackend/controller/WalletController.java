package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.dto.WalletDTO;
import pl.byczazagroda.trackexpensesappbackend.service.WalletService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for Wallet application.
 */
@RestController
@RequestMapping("api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    //TODO: szczegółowy mapping dla tej metody?
    //TODO: w jaki sposób mają być przekazywane parametry do metody?
    //TODO: walidacja parametrów metody - czy jest OK?
    @PatchMapping
    public ResponseEntity<WalletDTO> updateWallet(@NotNull @Min(value = 1) long id,
                                                  @NotNull @NotEmpty @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+") String name) {
        WalletDTO walletDTO = walletService.updateWallet(id, name);

        return ResponseEntity.ok(walletDTO);
    }

    //TODO: czy przenieść tą metodę do GlobalExceptionHandlerController?
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
