package pl.byczazagroda.trackexpensesappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;

import java.util.List;
import java.util.Optional;


public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByIdAndUserId(Long walletId, Long userId);

    List<Wallet> findAllByUserIdAndNameIsContainingIgnoreCase(Long userId, String name);

    List<Wallet> findAllByUserIdOrderByNameAsc(Long userId);

}
