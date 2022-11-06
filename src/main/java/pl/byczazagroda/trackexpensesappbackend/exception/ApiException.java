package pl.byczazagroda.trackexpensesappbackend.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

// klasa do zbudowania response
@Getter
@Setter
public class ApiException {
    @JsonIgnore
    private String profileName;
    private String businessStatus; // "W001",
    private String businessMessage; //  "WALLETS_RETRIEVING_ERROR",
    private String businessDescription; //  "Wallet with id: is not found in the database",
    private Integer businessStatusCode; //  404,

    public ApiException(
            String profileName, String businessStatus, String businessMessage,
            String businessDescription,
            Integer businessStatusCode) {
        this.profileName = profileName;
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessDescription = setBusinessDescriptionMethod(businessDescription);
        this.businessStatusCode = businessStatusCode;
    }

    @JsonIgnore
    private String setBusinessDescriptionMethod(String businessDescription) {
        if (profileName.equals("dev")) {
            return businessDescription;
        }
        return " ";
    }
}