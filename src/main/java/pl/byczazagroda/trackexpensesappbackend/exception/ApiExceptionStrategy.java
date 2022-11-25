package pl.byczazagroda.trackexpensesappbackend.exception;

import java.util.List;

public interface ApiExceptionStrategy {

    public String returnMessage(String message);

    public List<String> returnListMessage(List<String> list);
}
