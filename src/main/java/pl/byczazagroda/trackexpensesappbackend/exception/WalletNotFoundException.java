package pl.byczazagroda.trackexpensesappbackend.exception;

public class WalletNotFoundException extends AppRuntimeException{

    public WalletNotFoundException(String message) {
        super(message);
    }
}