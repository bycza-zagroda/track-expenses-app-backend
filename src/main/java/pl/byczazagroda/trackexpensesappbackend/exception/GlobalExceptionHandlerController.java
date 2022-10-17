package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandlerController exception handler for all application exceptions.
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandlerController {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String catchThrowableException(Throwable e) {
        log.error("Throwable", e);
        return e.getMessage();
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String catchAppException(AppException e) {
        log.error("AppException", e);
        return e.getMessage();
    }

    @ExceptionHandler(AppRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String catchAppRuntimeException(AppRuntimeException e) {
        log.error("AppRuntimeException", e);
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.getBindingResult().getFieldErrors().forEach(m -> {
            log.error(String.format("%s %s", m.getField(), m.getDefaultMessage()));
        });
        return e.getMessage();
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String resourceNotFoundHandler(ResourceNotFoundException e) {
        log.error(String.format("ResourceNotFoundException: %s", e.getMessage()));
        return e.getMessage();
    }

    @ExceptionHandler(ResourceNotSavedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String resourceNotSavedHandler(ResourceNotSavedException e) {
        log.error(String.format("ResourceNotSavedException: %s", e.getMessage()));
        return e.getMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        log.error(String.format("ResourceNotDeletedException: %s", e.getMessage()));
        return e.getMessage();
    }

    @ExceptionHandler(ResourceNotDeletedException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String resourceNotDeletedHandler(ResourceNotDeletedException e) {
        log.error(String.format("ResourceNotDeletedException: %s", e.getMessage()));
        return e.getMessage();
    }

    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String walletNotFoundHandler(WalletNotFoundException e) {
        log.error(String.format("WalletNotFoundException: %s", e.getMessage()));
        return e.getMessage();
    }
}
