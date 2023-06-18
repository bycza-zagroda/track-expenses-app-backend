package pl.byczazagroda.trackexpensesappbackend.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticatedService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticatedResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticatedResponse> register(@RequestBody AuthenticatedRequest request) {
        return ResponseEntity.ok(service.authenticated(request));
    }
}
