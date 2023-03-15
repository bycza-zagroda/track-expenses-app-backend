package pl.byczazagroda.trackexpensesappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;

@Repository
public interface FinancialTransactionCategoryRepository extends JpaRepository<FinancialTransactionCategory, Long> {
}