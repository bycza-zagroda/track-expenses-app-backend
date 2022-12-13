package pl.byczazagroda.trackexpensesappbackend.exception;

public class ResourceNotDeletedException extends AppRuntimeException{

    public ResourceNotDeletedException(String message){
        super(message);
    }
}
