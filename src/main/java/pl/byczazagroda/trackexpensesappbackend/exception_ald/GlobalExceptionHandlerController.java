//package pl.byczazagroda.trackexpensesappbackend.exception_ald;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
///**
// * GlobalExceptionHandlerController exception handler for all application exceptions.
// */
//@Slf4j
//@RestControllerAdvice
//class GlobalExceptionHandlerController {
//
//    @ExceptionHandler(Throwable.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String catchThrowableException(Throwable e) {
//        log.error("Throwable", e);
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(AppException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String catchAppException(AppException e) {
//        log.error("AppException", e);
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(AppRuntimeException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String catchAppRuntimeException(AppRuntimeException e) {
//        log.error("AppRuntimeException", e);
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        e.getBindingResult().getFieldErrors().forEach(m ->
//            log.error("error: {},{}", m.getField(), m.getDefaultMessage()));
//        return e.getMessage();
//    }
//
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String resourceNotFoundHandler(ResourceNotFoundException e) {
//        log.error(String.format("ResourceNotFoundException: %s", e.getMessage()));
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(ResourceNotSavedException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String resourceNotSavedHandler(ResourceNotSavedException e) {
//        log.error(String.format("ResourceNotSavedException: %s", e.getMessage()));
//        return e.getMessage();
//    }
//}
