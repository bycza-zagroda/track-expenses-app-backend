package pl.byczazagroda.trackexpensesappbackend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
@Table(name = "financial_transaction_categories")
public class FinancialTransactionCategory implements Serializable {

    /**
     * Class version 0.5.0.  SerialVersionUID needs to be updated with any change.
     */
    @Serial
    private static final long serialVersionUID = 100050L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^\\w+$")
    @Size(max = 30, message = "{validation.name.size.too_long}")
    private String name;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private FinancialTransactionType type;

    @Column(name = "creation_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Instant creationDate;

    @OneToMany(mappedBy = "financialTransactionCategory", fetch = FetchType.LAZY)
    private List<FinancialTransaction> financialTransactions;

    @PrePersist
    protected void onCreate() {
        creationDate = Instant.now();
    }
    
    public FinancialTransactionCategory(String name, FinancialTransactionType type) {
        this.name = name;
        this.type = type;
        this.creationDate = Instant.now();
    }

}
