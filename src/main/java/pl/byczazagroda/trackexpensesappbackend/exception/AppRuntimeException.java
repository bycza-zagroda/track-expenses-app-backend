package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppRuntimeException extends RuntimeException {

    private final String businessStatus; // "W001",
    private final String businessMessage; //  "WALLETS_RETRIEVING_ERROR",
    private final String description; //  "Wallet with id: is not found in the database",
    private final Integer businessStatusCode; //  404,

    public AppRuntimeException(ErrorCode error, String description) {
        super(error.getBusinessMessage());
        this.businessStatus = error.getBusinessStatus();
        this.businessMessage = error.getBusinessMessage();
        this.businessStatusCode = error.getBusinessStatusCode();
        this.description = description;
    }
}
