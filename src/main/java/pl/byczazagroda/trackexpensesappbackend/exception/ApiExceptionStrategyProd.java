package pl.byczazagroda.trackexpensesappbackend.exception;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Profile("prod")
public class ApiExceptionStrategyProd implements ApiExceptionStrategy {
    @Override
    public String returnMessage(String message) {
        return null;
    }

    @Override
    public List<String> returnListMessage(List<String> list) {
        return Collections.emptyList();

    }
}
