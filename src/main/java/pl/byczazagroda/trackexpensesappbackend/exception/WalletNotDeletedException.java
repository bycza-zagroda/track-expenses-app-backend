package pl.byczazagroda.trackexpensesappbackend.exception;

public class WalletNotDeletedException extends AppRuntimeException{

    public WalletNotDeletedException() {
        super("The wallet has not been deleted");
    }
}
