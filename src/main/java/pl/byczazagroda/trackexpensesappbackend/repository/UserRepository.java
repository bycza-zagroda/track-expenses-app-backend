package pl.byczazagroda.trackexpensesappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.byczazagroda.trackexpensesappbackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
