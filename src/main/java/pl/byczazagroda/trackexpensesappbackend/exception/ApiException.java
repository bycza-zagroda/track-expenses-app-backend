package pl.byczazagroda.trackexpensesappbackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
public class ApiException {

    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime zonedDateTime;

    public ApiException(String message,
                        HttpStatus httpStatus,
                        LocalDateTime zonedDateTime) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.zonedDateTime = zonedDateTime;
    }
}
