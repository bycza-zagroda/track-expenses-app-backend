package pl.byczazagroda.trackexpensesappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Validated
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    @Override
    public void registerUser(@Valid AuthRegisterDTO authRegisterDTO) {
        if (userRepository.existsByEmail(authRegisterDTO.email())) {
            throw new IllegalArgumentException("A user with the same email already exists");
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
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    public void validatePassword(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,100}";
        if (!password.matches(pattern)) {
            throw new IllegalArgumentException("Password does not meet strength requirements");
        }
    }
    @Override
    public String hashPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

}
