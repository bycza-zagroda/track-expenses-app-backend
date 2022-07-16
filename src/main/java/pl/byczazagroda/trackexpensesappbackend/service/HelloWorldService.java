package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelloWorldService {

    public String sayHello() {
        log.info("Hello World!");
        return "Hello World!";
    }

}
