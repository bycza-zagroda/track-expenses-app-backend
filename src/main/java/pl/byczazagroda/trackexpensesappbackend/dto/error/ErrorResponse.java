package pl.byczazagroda.trackexpensesappbackend.dto.error;

import java.util.List;

public interface ErrorResponse {

    String returnExceptionMessage(String message);

    List<String> returnExceptionMessageList(List<String> messageList);

    String returnExceptionDescription(String description);

    List<String> returnExceptionDescriptionList(List<String> descriptionList);
}
