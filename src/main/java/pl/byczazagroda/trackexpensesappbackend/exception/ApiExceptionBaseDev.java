package pl.byczazagroda.trackexpensesappbackend.exception;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ApiExceptionBaseDev create business exception message response
 * for profiles dev and test.
 */
@Component
@Profile({"dev", "test"})
public class ApiExceptionBaseDev implements ApiExceptionBase {
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
