package pl.byczazagroda.trackexpensesappbackend.exception;

public class ResourceNotFoundException extends AppRuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}