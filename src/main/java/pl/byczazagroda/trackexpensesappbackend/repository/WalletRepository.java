package pl.byczazagroda.trackexpensesappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;

import java.util.List;


public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findAllByNameLikeIgnoreCase(String name);
}
