package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.Getter;

@Getter
public class ApiException {

    private final String businessStatus; // "W001",
    private final String businessMessage; //  "WALLETS_RETRIEVING_ERROR",
//    private final String businessDescription; //  "Wallet with id: is not found in the database",
    private final Integer businessstatusCode; //  404,


    public ApiException(String businessStatus, String businessMessage, Integer businessstatusCode) {
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessstatusCode = businessstatusCode;
    }
}
//{ status: "W001",
// "message": "WALLETS_RETRIEVING_ERROR",
// "description": "Database query failed when retrieving wallets",
// "statusCode": 500 }