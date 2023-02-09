package pl.byczazagroda.trackexpensesappbackend.dto.error;

import lombok.Getter;
import lombok.Setter;

/**
 * ApiException create business exception message response
 * for cases beyond validation
 */
@Getter
@Setter
public class ErrorResponseDTO {

    private String status; // "W001",
    private String message; //  "WALLETS_RETRIEVING_ERROR",
    private String description; // "Wallet with id: is not found in the database",
    private Integer statusCode; // 404,

    public ErrorResponseDTO(String status, String message,
                            String description,
                            Integer statusCode) {
        this.status = status;
        this.message = message;
        this.description = description;
        this.statusCode = statusCode;
    }
}