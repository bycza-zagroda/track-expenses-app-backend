package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Wallet not found")

@Getter
@Setter
public class AppRuntimeException extends RuntimeException {

    public static final Logger LOGGER = LoggerFactory.getLogger(AppRuntimeException.class);
    Object obj;

    private String businessStatus; // "W001",
    private String businessMessage; //  "WALLETS_RETRIEVING_ERROR",

    private String businessDescription; //  "Wallet with id: is not found in the database",
    private Integer businessstatusCode; //  404,

    public AppRuntimeException(String businessStatus, String businessMessage, Integer businessstatusCode) {
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessstatusCode = businessstatusCode;
    }

    public AppRuntimeException(String businessStatus, String businessMessage,
                               String businessDescription, Integer businessstatusCode) {
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessDescription = businessDescription;
        this.businessstatusCode = businessstatusCode;
    }
    public AppRuntimeException(String businessStatus, String businessMessage, Integer businessstatusCode, Object obj) {
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessstatusCode = businessstatusCode;
        this.obj = obj;
    }

    public AppRuntimeException(String message) {
        super(message);
//        LOGGER.info(message);
    }

    public AppRuntimeException(String message, Object obj) {
        super(message);
        this.obj = obj;
//        LOGGER.error(message, obj);
    }
}
