package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.service.HelloWorldService;

//todo this Mock Class. It should be deleted before release 1.0.0
@RestController
@RequestMapping("api/hello")
@RequiredArgsConstructor
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    @GetMapping
    public String sayHello() {
        return helloWorldService.sayHello();
    }
}
