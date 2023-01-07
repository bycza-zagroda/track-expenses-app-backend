package pl.byczazagroda.trackexpensesappbackend.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * Entity class for money wallet.
 *
 * @version 0.2.0
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wallets")
public class Wallet implements Serializable {

    /**
     * Class version 0.3.0.  SerialVersionUID needs to be updated with any change.
     */
    @Serial
    private static final long serialVersionUID = 100030L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "[\\w ]+", message = "invalid input. Name should contains only latin litters")
    private String name;

    @Column(name = "creation_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Instant creationDate;

    @OneToMany(mappedBy = "wallet")
    @JoinColumn(name = "financial_transaction_id")
    private List<FinancialTransaction> financialTransactionList;

    public Wallet(String name) {
        this.name = name;
        this.creationDate = Instant.now();
    }
}