package pl.byczazagroda.trackexpensesappbackend.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


// klasa do zbudowania response

/**
 * Class to build Exceptions for application
 */
@Getter
@Setter
public class ApiException extends ApiExceptionBase{
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
        this.message = setMessageInApiException(profileName,message);
        this.description = setDescriptionInApiException(description);
        this.statusCode = statusCode;
    }

    /**
     * Method override setter for description
     *
     * @param description inform when and where exception is created
     *
     * @return null when spring profile is prod, otherwise full description for exception
     */
    private String setDescriptionInApiException(String description) {
        if (profileName.equals("prod")) {
            return null;
        }
        return description;
    }
}