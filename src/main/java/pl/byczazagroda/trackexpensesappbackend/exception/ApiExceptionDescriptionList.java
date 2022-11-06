package pl.byczazagroda.trackexpensesappbackend.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// klasa do zbudowania response
@Getter
@Setter
public class ApiExceptionDescriptionList {
    @JsonIgnore
    private String profileName;
    private String businessStatus; // "W001",
    private String businessMessage; //  "WALLETS_RETRIEVING_ERROR",
    private List<String> businessDescription; //  "Wallet with id: is not found in the database",
    private Integer businessStatusCode; //  404,

    public ApiExceptionDescriptionList(
            String profileName, String businessStatus, String businessMessage,
            List<String> businessDescription, Integer businessStatusCode) {
        this.profileName = profileName;
        this.businessStatus = businessStatus;
        this.businessMessage = businessMessage;
        this.businessDescription = setBusinessDescriptionMethod(businessDescription);
        this.businessStatusCode = businessStatusCode;
    }

    @JsonIgnore
    private List<String> setBusinessDescriptionMethod(List<String> businessDescription) {
        if (profileName.equals("dev")) {
            return businessDescription;
        }
        return new ArrayList<>();
    }
}