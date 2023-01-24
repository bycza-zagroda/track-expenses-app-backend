package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import pl.byczazagroda.trackexpensesappbackend.dto.error.ErrorResponse;
import pl.byczazagroda.trackexpensesappbackend.exception.ApiException;
import pl.byczazagroda.trackexpensesappbackend.exception.ApiExceptionDescriptionList;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * GlobalExceptionHandler Controller exception handler for all application exceptions.
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @Autowired
    private ErrorResponse errorResponse;

    /**
     * This handler is not used, ConstraintViolationException is handle as Throwable exception
     *
     * @param e ConstraintViolationException
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException: {}", e.getMessage());
        return new ResponseEntity<>(new ApiException(ErrorCode.TEA003.getBusinessStatus(), errorResponse.returnExceptionMessage(ErrorCode.TEA003.getBusinessMessage()), errorResponse.returnExceptionDescription(String.format("Throwable exception %s", e.getMessage())), ErrorCode.TEA003.getBusinessStatusCode()), HttpStatus.valueOf(ErrorCode.TEA003.getBusinessStatusCode()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowableException(Throwable ex) {
        log.error("handleThrowableException: {}", ex.getMessage());

        return new ResponseEntity<>(new ApiException(ErrorCode.TEA004.getBusinessStatus(), errorResponse.returnExceptionMessage(ErrorCode.TEA004.getBusinessMessage()), errorResponse.returnExceptionDescription(String.format("Throwable exception %s", ex.getMessage())), ErrorCode.TEA004.getBusinessStatusCode()), HttpStatus.valueOf(ErrorCode.TEA004.getBusinessStatusCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("handleHttpMessageNotReadableExceptionException: {}", ex.getMessage());

        return new ResponseEntity<>(new ApiException(ErrorCode.TEA003.getBusinessStatus(), errorResponse.returnExceptionMessage(ErrorCode.TEA003.getBusinessMessage()), errorResponse.returnExceptionDescription(String.format("HttpMessageNotReadableException exception %s", ex.getMessage())), ErrorCode.TEA003.getBusinessStatusCode()), HttpStatus.valueOf(ErrorCode.TEA003.getBusinessStatusCode()));
    }

    @ExceptionHandler(AppRuntimeException.class)
    public ResponseEntity<Object> handleAppRuntimeException(AppRuntimeException ex) {
        log.error("handleAppRuntimeException message: {}, object: {}", ex.getBusinessMessage(), ex.getDescription());
        return new ResponseEntity<>(new ApiException(ex.getBusinessStatus(), errorResponse.returnExceptionMessage(ex.getBusinessMessage()), errorResponse.returnExceptionDescription(ex.getDescription()), ex.getBusinessStatusCode()), HttpStatus.valueOf(ex.getBusinessStatusCode()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {

        log.error("handleNoHandlerFoundException message: {}, headers: {},  httpMethod: {}, request Url{}", ex.getMessage(), ex.getHeaders(), ex.getHttpMethod(), ex.getRequestURL());

        return new ResponseEntity<>(new ApiException(ErrorCode.TEA002.getBusinessStatus(), errorResponse.returnExceptionMessage(ErrorCode.TEA002.getBusinessMessage()), errorResponse.returnExceptionDescription(String.format("no handle for url %s", ex.getRequestURL())), ErrorCode.TEA002.getBusinessStatusCode()), HttpStatus.valueOf(ErrorCode.TEA002.getBusinessStatusCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        log.info("MethodArgumentNotValidException in: {}", ex.getObjectName());

        return new ResponseEntity<>(new ApiExceptionDescriptionList(ErrorCode.TEA003.getBusinessStatus(), errorResponse.returnExceptionMessage(ErrorCode.TEA003.getBusinessMessage()), errorResponse.returnExceptionDescriptionList(buildBusinessDescription(ex)), ErrorCode.TEA003.getBusinessStatusCode()), HttpStatus.valueOf(ErrorCode.TEA003.getBusinessStatusCode()));
    }

    private List<String> buildBusinessDescription(MethodArgumentNotValidException ex) {
        List<String> businessDescription = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(m -> {
            String description = String.format("error: field: %s, default message: %s, rejected value: %s", m.getField(), m.getDefaultMessage(), m.getRejectedValue());

            log.error(description);
            businessDescription.add(description);
        });
        return businessDescription;
    }

}
