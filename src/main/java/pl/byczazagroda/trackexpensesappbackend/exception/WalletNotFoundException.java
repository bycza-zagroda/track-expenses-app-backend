package pl.byczazagroda.trackexpensesappbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Wallet not found")
public class WalletNotFoundException extends AppRuntimeException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}
