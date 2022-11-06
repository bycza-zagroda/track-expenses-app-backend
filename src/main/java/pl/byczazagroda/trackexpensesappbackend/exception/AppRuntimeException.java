package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.Getter;
import lombok.Setter;

//@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Wallet not found")
@Getter
@Setter
public class AppRuntimeException extends RuntimeException {

    private String businessStatus; // "W001",
    private String businessMessage; //  "WALLETS_RETRIEVING_ERROR",
    private String businessDescription; //  "Wallet with id: is not found in the database",
    private Integer businessStatusCode; //  404,

    public AppRuntimeException(BusinessError error, String description) {
        super(error.getBusinessMessage());
        this.businessStatus = error.getBusinessStatus();
        this.businessMessage = error.getBusinessMessage();
        this.businessStatusCode = error.getBusinessStatusCode();
        this.businessDescription = description;
    }

}
