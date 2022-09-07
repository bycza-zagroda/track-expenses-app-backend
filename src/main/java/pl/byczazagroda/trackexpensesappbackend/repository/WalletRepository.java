package pl.byczazagroda.trackexpensesappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

}
