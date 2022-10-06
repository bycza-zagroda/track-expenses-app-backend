package pl.byczazagroda.trackexpensesappbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WalletExceptionHandler {

    @ExceptionHandler(WalletNotSavedException.class)
    public ResponseEntity<AppRuntimeException> walletNotSavedHandler(WalletNotSavedException exception) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(exception, httpStatus);
    }
}
