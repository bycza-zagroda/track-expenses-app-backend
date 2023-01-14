package pl.byczazagroda.trackexpensesappbackend.dto.error;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * ErrorResponseImpl create business exception message response
 * for profile prod. Exception message has: message and
 */
@Component
@Profile("prod")
public class ErrorResponseImpl implements ErrorResponse {
    @Override
    public String returnExceptionMessage(String message) {
        return null;
    }

    @Override
    public List<String> returnExceptionMessageList(List<String> list) {
        return Collections.emptyList();
    }

    @Override
    public String returnExceptionDescription(String description) {
        return null;
    }

    @Override
    public List<String> returnExceptionDescriptionList(List<String> descriptionList) {
        return Collections.emptyList();
    }
}
