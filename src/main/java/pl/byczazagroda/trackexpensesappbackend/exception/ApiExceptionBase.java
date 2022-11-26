package pl.byczazagroda.trackexpensesappbackend.exception;

import java.util.List;

public interface ApiExceptionBase {

    String returnExceptionMessage(String message);

    List<String> returnExceptionMessageList(List<String> messageList);

    String returnExceptionDescription(String description);

    List<String> returnExceptionDescriptionList(List<String> descriptionList);
}
