package pl.byczazagroda.trackexpensesappbackend.exception;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"dev", "test"})
public class ApiExceptionStrategyDev implements ApiExceptionStrategy {
    @Override
    public String returnMessage(String message) {
        return message;
    }

    @Override
    public List<String> returnListMessage(List<String> list) {
        return list;
    }
}
