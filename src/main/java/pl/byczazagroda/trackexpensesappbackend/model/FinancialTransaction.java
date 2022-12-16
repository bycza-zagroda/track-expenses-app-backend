package pl.byczazagroda.trackexpensesappbackend.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "financial_transactions")
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Wallet wallet;

    @Enumerated
    @Column(name = "transaction_type")
    private FinancialTransactionType transactionType;

    @DecimalMin("0.0")
    private BigDecimal amount;

    @Column(name = "creation_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Instant transactionDatetime;

    private String description;
}
