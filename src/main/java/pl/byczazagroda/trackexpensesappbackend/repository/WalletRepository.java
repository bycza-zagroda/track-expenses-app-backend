package pl.byczazagroda.trackexpensesappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;

/**
 * Repository for Wallet Entity.
 * @Since 0.2.0
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
