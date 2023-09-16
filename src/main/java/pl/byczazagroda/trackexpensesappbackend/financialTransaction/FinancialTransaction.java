package pl.byczazagroda.trackexpensesappbackend.financialTransaction;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.byczazagroda.trackexpensesappbackend.financialTransactionCategory.FinancialTransactionCategory;
import pl.byczazagroda.trackexpensesappbackend.wallet.api.Wallet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "financial_transactions")
public class FinancialTransaction implements Serializable {

    /**
     * Class version 0.3.0.  SerialVersionUID needs to be updated with any change.
     */
    @Serial
    private static final long serialVersionUID = 100030L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", columnDefinition = "ENUM('INCOME', 'EXPENSE')")
    private FinancialTransactionType type;

    @DecimalMin("0.0")
    private BigDecimal amount;

    @Column(name = "transaction_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Instant date;

    @Size(max = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private FinancialTransactionCategory financialTransactionCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialTransaction)) {
            return false;
        }
        return id != null && id.equals(((FinancialTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
