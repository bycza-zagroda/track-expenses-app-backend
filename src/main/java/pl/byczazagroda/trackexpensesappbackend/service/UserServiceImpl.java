package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.AppRuntimeException;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;
import pl.byczazagroda.trackexpensesappbackend.exception.ErrorCode;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void registerUser(@Valid AuthRegisterDTO authRegisterDTO) {
        if (userRepository.existsByEmail(authRegisterDTO.email())) {
            throw new AppRuntimeException(
                    ErrorCode.U001,
                    "A user with the email " + authRegisterDTO.email() + " already exists."
            );
        }
        validateEmail(authRegisterDTO.email());
        validatePassword(authRegisterDTO.password());

        UserStatus status = UserStatus.VERIFIED;
        String hashedPassword = hashPassword(authRegisterDTO.password());

        User user = new User();
        user.setEmail(authRegisterDTO.email());
        user.setPassword(hashedPassword);
        user.setUserName(authRegisterDTO.username());
        user.setUserStatus(status);

        userRepository.save(user);
    }

    public void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new AppRuntimeException(
                    ErrorCode.U002,
                    "Invalid email format for email: " + email
            );
        }
    }

    public void validatePassword(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,100}";
        if (!password.matches(pattern)) {
            throw new AppRuntimeException(
                    ErrorCode.U003,
                    "Password does not meet strength requirements."
            );
        }
    }

    @Override
    public String hashPassword(String password) {
        if (password.length() < 8) {
            throw new AppRuntimeException(
                    ErrorCode.U004,
                    "Password must be at least 8 characters."
            );
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

}
