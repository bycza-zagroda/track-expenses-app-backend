package pl.byczazagroda.trackexpensesappbackend.exception;

import liquibase.pro.packaged.bu;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * GlobalExceptionHandlerController exception handler for all application exceptions.
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandlerController {

    @Value("${spring.profiles.active}")
    private String profileName;

    @ExceptionHandler(AppRuntimeException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(AppRuntimeException ex) {

        log.error("message: {}, object: {}", ex.getBusinessMessage(), ex.getBusinessDescription());

        return new ResponseEntity<>(
                mapAppRuntimeExceptionToApiException(ex),
                HttpStatus.valueOf(ex.getBusinessStatusCode()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex) {

        log.error("message: {}, headers: {},  httpMethod: {}, request Url{}",
                ex.getMessage(), ex.getHeaders(), ex.getHttpMethod(), ex.getRequestURL());

        return new ResponseEntity<>(
                new ApiException(
                        this.profileName,
                        BusinessError.TEA002.getBusinessStatus(),
                        BusinessError.TEA002.getBusinessMessage(),
                        String.format("no handle for url %s", ex.getRequestURL()),
                        BusinessError.TEA002.getBusinessStatusCode()),
                HttpStatus.valueOf(BusinessError.TEA002.getBusinessStatusCode())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        return new ResponseEntity<>(
                new ApiExceptionDescriptionList(
                        this.profileName,
                        BusinessError.TEA003.getBusinessStatus(),
                        BusinessError.TEA003.getBusinessMessage(),
                        buildBusinessDescription(ex),
                        BusinessError.TEA003.getBusinessStatusCode()
                ),
                HttpStatus.valueOf(BusinessError.TEA003.getBusinessStatusCode()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        log.error(String.format("ResourceNotDeletedException: %s", e.getMessage()));
        return e.getMessage();
    }

    private ApiException mapAppRuntimeExceptionToApiException(AppRuntimeException e) {
        return new ApiException(
                this.profileName,
                e.getBusinessStatus(),
                e.getBusinessMessage(),
                e.getBusinessDescription(),
                e.getBusinessStatusCode());
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
//
//    private HttpStatus mapExceptionMessageCodeToHttpStatus(int code) {
//        return switch (code) {
//            case CODE_BAD_REQUEST -> HttpStatus.BAD_REQUEST;
//            case ExceptionMessage.CODE_NOT_FOUND -> HttpStatus.NOT_FOUND;
//            case ExceptionMessage.CODE_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
//            default -> HttpStatus.OK;
//        };
//    }

//    private HttpStatus mapExceptionMessageCodeToHttpStatus(int code) {
//        return switch (code) {
//            case CODE_BAD_REQUEST -> HttpStatus.BAD_REQUEST;
//            case ExceptionMessage.CODE_NOT_FOUND -> HttpStatus.NOT_FOUND;
//            case ExceptionMessage.CODE_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
//            default -> HttpStatus.OK;
//        };
//    }

//    @ExceptionHandler(Throwable.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String catchThrowableException(Throwable e) {
//        log.error("Throwable", e);
//        return e.getMessage();
//    }

//    @ExceptionHandler(AppRuntimeException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String catchAppRuntimeException(AppRuntimeException e) {
//        log.error("AppRuntimeException", e);
//        return e.getMessage();
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        e.getBindingResult().getFieldErrors().forEach(m ->
//                log.error("error: field: {}, default message:{}, agruments:{}, rejected value{}", m.getField(), m.getDefaultMessage(),
//                        m.getArguments(), m.getRejectedValue()));
//        return e.getMessage();
//    }

//    @ExceptionHandler(ResourceNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String resourceNotFoundHandler(ResourceNotFoundException e) {
//        log.error(String.format("ResourceNotFoundException: %s", e.getMessage()));
//        return e.getMessage();
//    }

//    @ExceptionHandler(value = {AppRuntimeException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Object> hendleApiException(AppRuntimeException ex) {
//
//        //1. paylod exception diteil
//        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
//        ApiException apiException = new ApiException(ex.getMessage(), ex,
//                badRequest, ZonedDateTime.now(ZoneId.of("Z")));
//        //2. return response entity
//        return new ResponseEntity<>(apiException, badRequest);
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleMethodArgumentNotValidExceptionMap(MethodArgumentNotValidException ex) {
//
//        HashMap<String, String> errorMap = new HashMap<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            errorMap.put(error.getField(), error.getDefaultMessage());
//        });
//        return errorMap;
//    }

    //    private HttpStatus mapExceptionMessageCodeToHttpStatus(String status) {
//        return switch (status) {
//            case ExceptionMessage.I001, ExceptionMessage.W001 -> HttpStatus.NOT_FOUND;
//            default -> HttpStatus.OK;
//        };
//    }
}
