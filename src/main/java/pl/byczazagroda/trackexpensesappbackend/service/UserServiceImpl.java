package pl.byczazagroda.trackexpensesappbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.regex.RegexConstant;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
@Validated
public class UserServiceImpl implements UserService {

    @Value("${jwt.exp}")
    private String expireTime;

    @Value("${jwt.refresh-exp}")
    private String refreshExpireTime;

    @Value("${jwt.secret}")
    private String secret;

    private static final int SHORTEST_PASSWORD_LENGTH = 8;

    private static final int GREATEST_PASSWORD_LENGTH = 100;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public void registerUser(@Valid AuthRegisterDTO authRegisterDTO) {
        if (!validateEmail(authRegisterDTO.email())) {
            throw new AppRuntimeException(
                    ErrorCode.U002,
                    "Invalid email format for email: " + authRegisterDTO.email()
            );
        }

        if (!validatePassword(authRegisterDTO.password())) {
            validatePasswordLength(authRegisterDTO.password());
            throw new AppRuntimeException(
                    ErrorCode.U003,
                    "Password doesn't meet requirements."
            );
        }

        if (userRepository.existsByEmail(authRegisterDTO.email())) {
            throw new AppRuntimeException(
                    ErrorCode.U001,
                    "A user with the email " + authRegisterDTO.email() + " already exists."
            );
        }
        String hashedPassword = hashPassword(authRegisterDTO.password());

        User user = User.builder()
                .email(authRegisterDTO.email())
                .password(hashedPassword)
                .userName(authRegisterDTO.username())
                .userStatus(UserStatus.VERIFIED)
                .build();
        userRepository.save(user);
    }
    @Override
    public String hashPassword(String password) {
        validatePasswordLength(password);

        return passwordEncoder.encode(password);
    }

    @Override
    public String loginUser(AuthLoginDTO authLoginDTO, HttpServletResponse response) {
        User u = userRepository.findByEmail(authLoginDTO.email())
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.U006,
                        "User with this email or password does not exist"));
        if (!passwordEncoder.matches(authLoginDTO.password(), u.getPassword())) {
            throw new AppRuntimeException(ErrorCode.U006, "User with this email or password does not exist");
        }
        if (authLoginDTO.isRememberMe()) {
            response.addCookie(createRefreshTokenCookie(u));
        }
        return createAccessToken(u);
    }

    private boolean validatePassword(String password) {

        return password.matches(RegexConstant.PASSWORD_PATTERN);
    }
    private boolean validateEmail(String email) {

        return email.matches(RegexConstant.EMAIL_PATTERN);
    }

    private String createAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getId().toString())
                .withExpiresAt(Instant.now().plusMillis(Long.parseLong(expireTime)))
                .withClaim("token_type", "AUTH")
                .withIssuedAt(Instant.now())
                .withClaim("username", user.getUserName())
                .withClaim("email", user.getEmail())
                .withClaim("authorities", List.of())
                .sign(Algorithm.HMAC256(secret));
    }

    private Cookie createRefreshTokenCookie(User user) {
        String token = JWT.create()
                .withSubject(user.getId().toString())
                .withExpiresAt(Instant.now().plusMillis(Long.parseLong(refreshExpireTime)))
                .withClaim("token_type", "REFRESH")
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(secret));
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void validatePasswordLength(String password) {
        if (password.length() < SHORTEST_PASSWORD_LENGTH) {
            throw new AppRuntimeException(
                    ErrorCode.U004,
                    "Password must be at least 8 characters."
            );
        } else if (password.length() > GREATEST_PASSWORD_LENGTH) {
            throw new AppRuntimeException(ErrorCode.U007,
                    "Password must consist of no more that 100 characters."
            );
        }
    }

}
