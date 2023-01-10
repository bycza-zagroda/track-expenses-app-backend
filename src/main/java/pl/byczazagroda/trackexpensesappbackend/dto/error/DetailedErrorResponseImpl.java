package pl.byczazagroda.trackexpensesappbackend.dto.error;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DetailedErrorResponse create business exception message response for profiles dev and test.
 */
@Component
@Profile({"dev", "test"})
public class DetailedErrorResponseImpl implements ErrorResponse {
    @Override
    public String returnExceptionMessage(String message) {
        return message;
    }

    @Override
    public List<String> returnExceptionMessageList(List<String> list) {
        return list;
    }

    @Override
    public String returnExceptionDescription(String description) {
        return description;
    }

    @Override
    public List<String> returnExceptionDescriptionList(List<String> descriptionList) {
        return descriptionList;
    }
}
