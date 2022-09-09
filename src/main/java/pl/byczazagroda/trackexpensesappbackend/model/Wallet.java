package pl.byczazagroda.trackexpensesappbackend.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Entity class for money wallet.
 *
 * @author DawidStuzynski
 * @version 1.0
 * @since track-expenses-app-backend 2.0
 */

@Entity
@Getter
@NoArgsConstructor
public class Wallet implements Serializable {

    /**
     * Class version 1.0 -> 100010L
     */
    @Serial
    private static final long serialVersionUID = 100010L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "[a-z A-Z]+")
    private String name;

    private Instant creationDate;

    public Wallet(String name) {
        this.name = name;
        this.creationDate = Instant.now();
    }
}
