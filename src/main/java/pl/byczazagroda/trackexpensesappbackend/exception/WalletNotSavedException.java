package pl.byczazagroda.trackexpensesappbackend.exception;

public class WalletNotSavedException extends AppRuntimeException {

    public WalletNotSavedException() {
        super("Sorry. Something went wrong and your Wallet is not saved. Contact administrator.");
    }
}
