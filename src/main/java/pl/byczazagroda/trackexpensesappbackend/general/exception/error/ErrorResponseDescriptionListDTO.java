package pl.byczazagroda.trackexpensesappbackend.general.exception.error;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ApiExceptionDescriptionList create business exception message response
 * for validation cases
 */
@Getter
@Setter
public class ErrorResponseDescriptionListDTO {
    private String status; // "W001",
    private String message; // "WALLETS_RETRIEVING_ERROR",
    private List<String> description; // "Wallet with id: is not found in the database",
    private Integer statusCode; // 404,

    public ErrorResponseDescriptionListDTO(
            String status, String message,
            List<String> description, Integer statusCode) {
        this.status = status;
        this.message = message;
        this.description = description;
        this.statusCode = statusCode;
    }
}