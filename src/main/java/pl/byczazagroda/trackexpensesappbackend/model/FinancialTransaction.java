package pl.byczazagroda.trackexpensesappbackend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToOne
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private FinancialTransactionType financialTransactionType;

    @DecimalMin("0.0")
    private BigDecimal amount;

    @Column(name = "transaction_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Instant transactionDate;

    private String description;
}
