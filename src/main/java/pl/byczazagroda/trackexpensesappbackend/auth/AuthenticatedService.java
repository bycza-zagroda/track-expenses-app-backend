package pl.byczazagroda.trackexpensesappbackend.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.config.JwtService;
import pl.byczazagroda.trackexpensesappbackend.model.Role;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticatedService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticatedResponse register(RegisterRequest request) {

        final User build = User.builder()
                .userName(request.getFirstName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(build);

        var jwtToken = jwtService.generateToken(build);
        return AuthenticatedResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticatedResponse authenticated(AuthenticatedRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );


        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticatedResponse.builder()
                .token(jwtToken).build();
    }
}
