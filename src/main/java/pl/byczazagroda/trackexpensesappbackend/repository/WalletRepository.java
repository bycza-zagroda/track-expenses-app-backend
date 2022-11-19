package pl.byczazagroda.trackexpensesappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;

import java.util.List;

/**
 * Repository for Wallet Entity.
 * @Since 0.2.0
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT w FROM Wallet w WHERE w.name LIKE %:name%")
    List<Wallet> findByLikeName(@Param("name") String name);
}
