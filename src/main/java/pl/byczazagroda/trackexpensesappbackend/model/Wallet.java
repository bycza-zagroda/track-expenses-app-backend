package pl.byczazagroda.trackexpensesappbackend.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

@Entity
@Getter
@NoArgsConstructor
public class Wallet implements Serializable {

    //class version 100010
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
