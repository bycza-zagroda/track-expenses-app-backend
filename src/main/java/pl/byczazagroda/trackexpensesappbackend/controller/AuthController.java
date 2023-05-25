package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthAccessTokenDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;
import pl.byczazagroda.trackexpensesappbackend.service.UserServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthAccessTokenDTO> authenticateUser(@Valid @RequestBody AuthLoginDTO authLoginDTO) {
        //TODO
        return new ResponseEntity<>(new AuthAccessTokenDTO("TODO-accessTOKEN"), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser() {
        //TODO
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody AuthRegisterDTO authRegisterDTO) {
        userService.registerUser(authRegisterDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthAccessTokenDTO> refreshToken(@CookieValue(name = "refresh_token") String refreshToken) {
        //TODO
        return new ResponseEntity<>(new AuthAccessTokenDTO("TODO-accessTOKEN"), HttpStatus.OK);
    }
}
