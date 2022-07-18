package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.byczazagroda.trackexpensesappbackend.exception.AppException;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;

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

}
