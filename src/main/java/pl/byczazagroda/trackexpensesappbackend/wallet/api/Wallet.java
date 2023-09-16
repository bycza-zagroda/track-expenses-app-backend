package pl.byczazagroda.trackexpensesappbackend.wallet.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class for money wallet.
 *
 * @version 0.2.0
 */

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "[\\w ]+", message = "invalid input. Name should contains only latin litters")
    private String name;

    @Column(name = "creation_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Instant creationDate;

    @OneToMany(mappedBy = "wallet",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<FinancialTransaction> financialTransactionList = new ArrayList<>();

    public Wallet(String name, User user) {
        this.name = name;
        this.user = user;
        this.creationDate = Instant.now();
    }

    public void addFinancialTransaction(FinancialTransaction financialTransaction) {
        this.financialTransactionList.add(financialTransaction);
        financialTransaction.setWallet(this);
    }

    public void removeFinancialTransaction(FinancialTransaction financialTransaction) {
        this.financialTransactionList.remove(financialTransaction);
        financialTransaction.setWallet(null);
    }
}