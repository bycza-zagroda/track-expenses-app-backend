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
import java.sql.Timestamp;
import java.time.Instant;

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
     * Class version 0.1.0.  SerialVersionUID needs to be updated with any change.
     */
    @Serial
    private static final long serialVersionUID = 100010L;

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

    public Wallet(String name) {
        this.name = name;
        this.creationDate = Instant.now();
    }
}