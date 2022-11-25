package pl.byczazagroda.trackexpensesappbackend.exception;

import org.springframework.context.annotation.Profile;

/**
 * Utility class for Exceptions
 */
@Profile("prod")
public class ApiExceptionBase {

    protected String setMessageInApiException(String profileName,String message) {
        if (profileName.equals("prod")) {
            return null;
        }
        return message;
    }
}
