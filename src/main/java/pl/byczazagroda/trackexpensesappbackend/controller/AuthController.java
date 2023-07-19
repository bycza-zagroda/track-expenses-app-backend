package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthAccessTokenDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthAccessTokenDTO> authenticateUser(@Valid @RequestBody AuthLoginDTO authLoginDTO,
                                                               HttpServletResponse response) {
        String accessToken = userService.loginUser(authLoginDTO, response);
        return new ResponseEntity<>(new AuthAccessTokenDTO(accessToken), HttpStatus.OK);
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
    public ResponseEntity<AuthAccessTokenDTO> refreshToken(
            @CookieValue(name = "refresh_token") String refreshToken,
            @RequestHeader(name = "Authorization") String accessToken,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
            String newAccessToken = userService.refreshToken(request, response, refreshToken, accessToken);
            AuthAccessTokenDTO accessTokenDTO = new AuthAccessTokenDTO(newAccessToken);

            return ResponseEntity.ok(accessTokenDTO);
    }

}
