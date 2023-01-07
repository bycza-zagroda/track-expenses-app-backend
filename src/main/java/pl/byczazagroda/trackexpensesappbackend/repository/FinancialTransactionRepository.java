package pl.byczazagroda.trackexpensesappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

}
