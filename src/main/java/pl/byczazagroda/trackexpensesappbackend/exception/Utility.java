package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.experimental.UtilityClass;

/**
 * Utility class for Exceptions
 */
@UtilityClass
public class Utility {

    protected static String setMessageInApiException(String profileName,String message) {
        if (profileName.equals("prod")) {
            return null;
        }
        return message;
    }
}
