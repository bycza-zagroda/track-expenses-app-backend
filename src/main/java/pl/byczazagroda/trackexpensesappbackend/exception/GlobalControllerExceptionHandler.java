package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * GlobalExceptionHandlerController exception handler for all application exceptions.
 */
@Slf4j
@RestControllerAdvice
class GlobalControllerExceptionHandler {

    @Autowired
    private ApiExceptionBase apiExceptionBase;

    /**
     * This handler is not used, ConstraintViolationException is handle as Throwable exception
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ResponseEntity<Object> handleThrowableException(Throwable ex) {
        log.error("handleThrowableException: {}", ex.getMessage());

        return new ResponseEntity<>(
                new ApiException(
                        ErrorCode.TEA004.getBusinessStatus(),
                        apiExceptionBase.returnExceptionMessage(ErrorCode.TEA004.getBusinessMessage()),
                        apiExceptionBase.returnExceptionDescription(String.format("Throwable exception %s", ex.getMessage())),
                        ErrorCode.TEA004.getBusinessStatusCode()),
                HttpStatus.valueOf(ErrorCode.TEA004.getBusinessStatusCode())
        );
    }

    @ExceptionHandler(AppRuntimeException.class)
    public ResponseEntity<Object> handleAppRuntimeException(AppRuntimeException ex) {
        log.error(
                "handleAppRuntimeException message: {}, object: {}", ex.getBusinessMessage(),
                ex.getDescription());
        return new ResponseEntity<>(
                new ApiException(
                        ex.getBusinessStatus(),
                        apiExceptionBase.returnExceptionMessage(ex.getBusinessMessage()),
                        apiExceptionBase.returnExceptionDescription(ex.getDescription()),
                        ex.getBusinessStatusCode()),
                HttpStatus.valueOf(ex.getBusinessStatusCode()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex) {

        log.error(
                "handleNoHandlerFoundException message: {}, headers: {},  httpMethod: {}, request Url{}",
                ex.getMessage(), ex.getHeaders(), ex.getHttpMethod(), ex.getRequestURL());

        return new ResponseEntity<>(
                new ApiException(
                        ErrorCode.TEA002.getBusinessStatus(),
                        apiExceptionBase.returnExceptionMessage(ErrorCode.TEA002.getBusinessMessage()),
                        apiExceptionBase.returnExceptionDescription(String.format("no handle for url %s", ex.getRequestURL())),
                        ErrorCode.TEA002.getBusinessStatusCode()),
                HttpStatus.valueOf(ErrorCode.TEA002.getBusinessStatusCode())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException (
            MethodArgumentNotValidException ex) {

        log.info("MethodArgumentNotValidException in: {}", ex.getObjectName());

        return new ResponseEntity<>(
                new ApiExceptionDescriptionList(
                        ErrorCode.TEA003.getBusinessStatus(),
                        apiExceptionBase.returnExceptionMessage(ErrorCode.TEA003.getBusinessMessage()),
                        apiExceptionBase.returnExceptionDescriptionList(buildBusinessDescription(ex)),
                        ErrorCode.TEA003.getBusinessStatusCode()
                ),
                HttpStatus.valueOf(ErrorCode.TEA003.getBusinessStatusCode()));
    }

    private List<String> buildBusinessDescription(MethodArgumentNotValidException ex) {
        List<String> businessDescription = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(m -> {
            String description = String.format(
                    "error: field: %s, default message: %s, rejected value: %s",
                    m.getField(),
                    m.getDefaultMessage(),
                    m.getRejectedValue());

            log.error(description);
            businessDescription.add(description);
        });
        return businessDescription;
    }

}
