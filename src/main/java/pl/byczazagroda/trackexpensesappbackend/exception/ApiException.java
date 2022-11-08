package pl.byczazagroda.trackexpensesappbackend.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

// klasa do zbudowania response
@Getter
@Setter
public class ApiException {
    @JsonIgnore
    private String profileName;
    private String status; // "W001",
    private String message; //  "WALLETS_RETRIEVING_ERROR",
    private String description; //  "Wallet with id: is not found in the database",
    private Integer statusCode; //  404,

    public ApiException(
            String profileName, String status, String message,
            String description,
            Integer statusCode) {
        this.profileName = profileName;
        this.status = status;
        this.message = message;
        this.description = setDescriptionMethod(description);
        this.statusCode = statusCode;
    }

    @JsonIgnore
    private String setDescriptionMethod(String description) {
        if (profileName.equals("dev") || profileName.equals("test")) {
            return description;
        }
        return " ";
    }
}