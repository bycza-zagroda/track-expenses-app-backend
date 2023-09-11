package pl.byczazagroda.trackexpensesappbackend;

import pl.byczazagroda.trackexpensesappbackend.model.User;
import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;
import pl.byczazagroda.trackexpensesappbackend.repository.UserRepository;

public class IntegrationTestUtils {

      public static User createTestUser(UserRepository userRepository) {
        final User userOne = User.builder()
                .userName("UserOne")
                .email("user@server.domain.com")
                .password("Password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();

        return userRepository.save(userOne);
    }

    public static User createTestUserWithEmail(UserRepository userRepository, String email) {
        final User userOne = User.builder()
                .userName("UserOne")
                .email(email)
                .password("Password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();

        return userRepository.save(userOne);
    }
}
