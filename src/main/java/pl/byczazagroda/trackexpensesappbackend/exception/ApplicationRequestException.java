package pl.byczazagroda.trackexpensesappbackend.exception;

public class ApplicationRequestException extends RuntimeException {

    private Object obj;

    public ApplicationRequestException(String message) {
        super(message);
    }
    public ApplicationRequestException(String message, Object obj) {
        super(message);
        this.obj = obj;
    }

    public ApplicationRequestException(String message, Throwable cause) {
        super(message, cause);
    }


}
