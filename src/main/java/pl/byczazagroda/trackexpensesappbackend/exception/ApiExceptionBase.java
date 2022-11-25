package pl.byczazagroda.trackexpensesappbackend.exception;

import org.springframework.context.annotation.Profile;

/**
 * Class for Exception's profile prod
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
