package pl.byczazagroda.trackexpensesappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionCategory;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialTransactionCategoryRepository extends JpaRepository<FinancialTransactionCategory, Long> {
    @Query(nativeQuery = false, value = "select c from FinancialTransactionCategory c where c.id = :id and c.user.id = :userId")
    Optional<FinancialTransactionCategory> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query(nativeQuery = false, value = "select c from FinancialTransactionCategory c where c.user.id = :userId")
    Optional<List <FinancialTransactionCategory>> findAllByUserId(Long userId);

    @Query(value = "SELECT COUNT(c) > 0 as result FROM FinancialTransactionCategory c WHERE c.id = :id AND c.user.id = :userId")
    boolean existsByIdAndUserId(Long id, Long userId);

}
