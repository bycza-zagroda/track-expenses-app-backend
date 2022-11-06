package pl.byczazagroda.trackexpensesappbackend.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

public enum BusinessError {

    W001("W001", "WALLET_RETRIEVING_ERROR", 200),
    W003("W003", "WALLET_NOT_FOUND", 404),

    I001("I001", "INCOME_NOT_FOUND", 404),

    TEA001("TEA001", "INTERNAL_SERVER_ERROR", 500),
    TEA002("TEA002", "ENDPOINT_DOES_NOT_EXISTS", 400),
    TEA003("TEA003", "VALIDATION_FAILED", 400);

    private String businessStatus;
    private String businessMessage;
    private Integer businessStatusCode;

    BusinessError(String businessStatus, String businessMessage, Integer businessStatusCode) {
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessStatusCode = businessStatusCode;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public String getBusinessMessage() {
        return businessMessage;
    }

    public Integer getBusinessStatusCode() { return businessStatusCode; }
}
