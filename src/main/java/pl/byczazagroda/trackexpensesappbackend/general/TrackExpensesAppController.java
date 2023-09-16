package pl.byczazagroda.trackexpensesappbackend.general;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackExpensesAppController {

    @Value("${application.version}")
    private String version;

    @Value("${application.group}")
    private String group;

    @GetMapping("/")
    public String sayHello() {

        return String.format("TrackExpensesApp by %s version %s", group, version);
    }
}

