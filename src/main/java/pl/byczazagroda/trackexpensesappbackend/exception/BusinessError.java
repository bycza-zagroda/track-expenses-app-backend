package pl.byczazagroda.trackexpensesappbackend.exception;

public enum BusinessError {

    W001("W001", "WALLET_RETRIEVING_ERROR"),

    I001("I001", "INCOME_NOT_FOUND");

    private String businessStatus;
    private String businessMessage;

    BusinessError(String businessStatus, String businessMessage) {
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public String getBusinessMessage() {
        return businessMessage;
    }
}
