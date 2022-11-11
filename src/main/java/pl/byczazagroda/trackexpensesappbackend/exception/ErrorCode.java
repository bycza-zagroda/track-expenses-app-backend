package pl.byczazagroda.trackexpensesappbackend.exception;

public enum ErrorCode {

    W001("W001", "WALLET_RETRIEVING_ERROR", 200),
    W003("W003", "WALLET_NOT_FOUND", 404),

    I001("I001", "INCOME_NOT_FOUND", 404),

    TEA001("TEA001", "INTERNAL_SERVER_ERROR", 500),
    TEA002("TEA002", "ENDPOINT_DOES_NOT_EXISTS", 400),
    TEA003("TEA003", "VALIDATION_FAILED", 400),
    TEA004("TEA004", "THROWABLE_EXCEPTION", 500);

    private final String businessStatus;
    private final String businessMessage;
    private final Integer businessStatusCode;

    ErrorCode(String businessStatus, String businessMessage, Integer businessStatusCode) {
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
