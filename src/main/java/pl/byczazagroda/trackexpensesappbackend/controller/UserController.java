package pl.byczazagroda.trackexpensesappbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<Void> ourTest() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
