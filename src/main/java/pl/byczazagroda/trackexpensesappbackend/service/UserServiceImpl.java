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

@RequiredArgsConstructor
@Service
@Validated
public class UserServiceImpl implements UserService {

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
            int passwordLength = authRegisterDTO.password().length();
            if (passwordLength < 8) {
                throw new AppRuntimeException(
                        ErrorCode.U004,
                        "Password must be at least 8 characters."
                );
            } else if (passwordLength > 100) {
                throw new AppRuntimeException(
                        ErrorCode.U006,
                        "Password must consist of less than 100 characters."
                );
            }

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

    private boolean validateEmail(String email) {
        String pattern = "^[A-Za-z0-9+_.-]+@(.+)$";

        return email.matches(pattern);
    }

    private boolean validatePassword(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,100}";

        return password.matches(pattern);
    }

    @Override
    public String hashPassword(String password) {
        if (password.length() < 8) {
            throw new AppRuntimeException(
                    ErrorCode.U004,
                    "Password must be at least 8 characters."
            );
        } else if (password.length() > 100) {
            throw new AppRuntimeException(ErrorCode.U006,
            "Password must consist of no more that 100 characters."
            );
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode(password);
    }

}
