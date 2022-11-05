package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class ResourceNotSaveException extends RuntimeException{
    public ResourceNotSaveException(String message) {
        super(message);
    }

    public ResourceNotSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    //    @ExceptionHandler(WalletNotSavedException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String walletNotSavedHandler(WalletNotSavedException e) {
//        log.error("WalletNotSavedException", e);
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(WalletNotDeletedException.class)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public String walletNotDeletedHandler(WalletNotDeletedException e) {
//        log.error("WalletNotDeletedException: {}", e.getMessage());
//        return e.getMessage();
//    }
}
