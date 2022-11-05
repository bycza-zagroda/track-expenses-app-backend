package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.Getter;
import lombok.Setter;


// klasa do zbudowania response
@Getter
@Setter
public class ApiException {
    private String businessStatus; // "W001",
    private String businessMessage; //  "WALLETS_RETRIEVING_ERROR",

    private String businessDescription; //  "Wallet with id: is not found in the database",
    private Integer businessstatusCode; //  404,

    public ApiException(String businessStatus, String businessMessage, Integer businessstatusCode) {
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessstatusCode = businessstatusCode;
    }
    public ApiException(String businessStatus, String businessMessage, String businessDescription, Integer businessstatusCode) {
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessDescription = businessDescription;
        this.businessstatusCode = businessstatusCode;
    }
}