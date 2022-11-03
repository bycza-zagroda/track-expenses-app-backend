package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static pl.byczazagroda.trackexpensesappbackend.exception.BusinessError.W001;
import static pl.byczazagroda.trackexpensesappbackend.exception.ExceptionMessage.*;

/**
 * GlobalExceptionHandlerController exception handler for all application exceptions.
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("message: {}, object: {}", ex.getMessage(), ex.obj);



        return new ResponseEntity<>(new ApiException(W001.getBusinessStatus(), W001.getBusinessMessage(), CODE_NOT_FOUND ),
                mapExceptionMessageCodeToHttpStatus(CODE_NOT_FOUND));
    }

    //    public ApiException(String businessStatus, String businessMessage, Integer businessstatusCode) {
//to jest gdy nie istnieje taki controlller, taki endpoint
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        log.error("message: {}, headers: {},  httpMethod: {}, request Url{}",
                ex.getMessage(), ex.getHeaders(), ex.getHttpMethod(), ex.getRequestURL());
        return new ResponseEntity<>
                (new ApiException(ex.getMessage(), ex.getHttpMethod(), CODE_NOT_FOUND
                       ),
                        mapExceptionMessageCodeToHttpStatus(CODE_NOT_FOUND));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        ex.getBindingResult().getFieldErrors().forEach(m ->
                log.error("error: field: {}, default message:{}, rejected value{}", m.getField(), m.getDefaultMessage(),
                        m.getRejectedValue()));
        return new ResponseEntity<>(new ApiException(ex.getMessage(), ex.getObjectName()
                , ex.getErrorCount()
        ),
                HttpStatus.BAD_REQUEST);
    }

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
    private HttpStatus mapExceptionMessageCodeToHttpStatus(int code) {
        return switch (code) {
            case ExceptionMessage.CODE_BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case ExceptionMessage.CODE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case ExceptionMessage.CODE_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.OK;
        };
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
}
