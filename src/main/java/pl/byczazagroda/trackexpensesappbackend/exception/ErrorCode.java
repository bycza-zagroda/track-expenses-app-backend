package pl.byczazagroda.trackexpensesappbackend.exception;

public enum ErrorCode {

    W001("W001", "WALLET_RETRIEVING_ERROR", 200),
    W003("W003", "WALLET_NOT_FOUND", 404),

    W004("W004", "WALLETS_LIST_LIKE_NAME_NOT_FOUND_EXC_MSG", 404),

    FT001("FT001", "FINANCIAL_TRANSACTION_NOT_FOUND", 404),

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
        return this.businessStatus;
    }

    public String getBusinessMessage() {
        return this.businessMessage;
    }

    public Integer getBusinessStatusCode() {
        return this.businessStatusCode;
    }
}
