package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
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
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;

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
    public ResponseEntity<AuthAccessTokenDTO> refreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");

        if (userService.validateToken(accessToken) && userService.validateToken(refreshToken)) {
            User user = userService.getUserFromToken(accessToken);

            String newAccessToken = userService.createAccessToken(user);
            Cookie newRefreshTokenCookie = userService.createRefreshTokenCookie(user);

            response.addCookie(newRefreshTokenCookie);

            return new ResponseEntity<>(new AuthAccessTokenDTO(newAccessToken), HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
