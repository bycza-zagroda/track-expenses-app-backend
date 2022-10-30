package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Wallet not found")

public class ResourceNotFoundException extends RuntimeException {
    public static final Logger LOGGER = LoggerFactory.getLogger(ResourceNotFoundException.class);
    Object obj;
    public ResourceNotFoundException(String message) {
        super(message);
//        LOGGER.info(message);
    }

    public ResourceNotFoundException(String message, Object obj) {
        super(message);
        this.obj = obj;
//        LOGGER.error(message, obj);
    }
}
