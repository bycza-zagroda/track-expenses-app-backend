package pl.byczazagroda.trackexpensesappbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthAccessTokenDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private final long expirationTime;

    private final String secret;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public UserServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           @Value("${jwt.expirationTime}") long expirationTime,
                           @Value("${jwt.secret}") String secret) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.expirationTime = expirationTime;
        this.secret = secret;
    }


    @Override
    public AuthAccessTokenDTO authenticateUser(AuthLoginDTO authLoginDTO) {
        return authenticate(authLoginDTO.email(), authLoginDTO.password());

    }


    private AuthAccessTokenDTO authenticate(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Błąd w userSerivceimpl"));
        System.err.println("User from UserServiceImpl " + user);
        Authentication authenticate = null;
        try {

            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getId(), password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        User principal = (User) authenticate.getPrincipal();
        String token = JWT.create()
                .withSubject(String.valueOf(principal.getId()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));
        return new AuthAccessTokenDTO(token);
    }

}
