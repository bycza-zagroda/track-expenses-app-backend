package pl.byczazagroda.trackexpensesappbackend.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serial;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "financial_transactions_categories")
public class FinancialTransactionCategory {
    /**
     * Class version 0.1.0.  SerialVersionUID needs to be updated with any change.
     */
    @Serial
    private static final long serialVersionUID = 100000L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "creation_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Date creation_date;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private FinancialTransactionType type;

    @PrePersist
    protected void onCreate() {
        creation_date = new Date();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialTransactionCategory)) {
            return false;
        }
        return id != null && id.equals(((FinancialTransactionCategory) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
