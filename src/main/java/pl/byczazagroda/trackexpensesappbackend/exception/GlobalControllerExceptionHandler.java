package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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
    Environment environment;

    @Value("${spring.profiles.active}")
    private String profileName;

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
        profileName = environment.getActiveProfiles()[0];
        log.error("handleThrowableException: {}", ex.getMessage());

        return new ResponseEntity<>(
                new ApiException(
                        this.profileName,
                        ErrorCode.TEA004.getBusinessStatus(),
                        ErrorCode.TEA004.getBusinessMessage(),
                        String.format("Throwable exception %s", ex.getMessage()),
                        ErrorCode.TEA004.getBusinessStatusCode()),
                HttpStatus.valueOf(ErrorCode.TEA004.getBusinessStatusCode())
        );
    }

    @ExceptionHandler(AppRuntimeException.class)
    public ResponseEntity<Object> handleAppRuntimeException(AppRuntimeException ex) {
        log.error(
                "handleAppRuntimeException message: {}, object: {}", ex.getBusinessMessage(),
                ex.getBusinessDescription());
        return new ResponseEntity<>(
                new ApiException(
                        this.profileName,
                        ex.getBusinessStatus(),
                        ex.getBusinessMessage(),
                        ex.getBusinessDescription(),
                        ex.getBusinessStatusCode()),
                HttpStatus.valueOf(ex.getBusinessStatusCode()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex) {
        profileName = environment.getActiveProfiles()[0];

        log.error(
                "handleNoHandlerFoundException message: {}, headers: {},  httpMethod: {}, request Url{}",
                ex.getMessage(), ex.getHeaders(), ex.getHttpMethod(), ex.getRequestURL());

        return new ResponseEntity<>(
                new ApiException(
                        this.profileName,
                        ErrorCode.TEA002.getBusinessStatus(),
                        ErrorCode.TEA002.getBusinessMessage(),
                        String.format("no handle for url %s", ex.getRequestURL()),
                        ErrorCode.TEA002.getBusinessStatusCode()),
                HttpStatus.valueOf(ErrorCode.TEA002.getBusinessStatusCode())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        profileName = environment.getActiveProfiles()[0];

        return new ResponseEntity<>(
                new ApiExceptionDescriptionList(
                        this.profileName,
                        ErrorCode.TEA003.getBusinessStatus(),
                        ErrorCode.TEA003.getBusinessMessage(),
                        null,
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
