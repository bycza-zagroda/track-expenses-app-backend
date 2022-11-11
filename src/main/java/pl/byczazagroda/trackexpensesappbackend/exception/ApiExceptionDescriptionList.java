package pl.byczazagroda.trackexpensesappbackend.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static pl.byczazagroda.trackexpensesappbackend.exception.Utility.setMessageInApiException;

// klasa do zbudowania response
@Getter
@Setter
public class ApiExceptionDescriptionList {
    @JsonIgnore
    private String profileName;
    private String businessStatus; // "W001",
    private String businessMessage; //  "WALLETS_RETRIEVING_ERROR",
    private List<String> descriptionList; //  "Wallet with id: is not found in the database",
    private Integer businessStatusCode; //  404,
    public ApiExceptionDescriptionList(
            String profileName, String businessStatus, String businessMessage,
            List<String> descriptionList, Integer businessStatusCode) {
        this.profileName = profileName;
        this.businessStatus = businessStatus;
        this.businessMessage = setMessageInApiException(profileName,businessMessage);
        this.descriptionList = setBusinessDescriptionMethod(descriptionList);
        this.businessStatusCode = businessStatusCode;
    }
    private List<String> setBusinessDescriptionMethod(List<String> descriptionList) {
        if (profileName.equals("prod")) {
            return Collections.emptyList();
        }
        return  descriptionList;
    }
}