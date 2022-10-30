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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.DATE_TIME;

/**
 * Entity class for money wallet.
 *
 * @version 0.2.0
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @Pattern(regexp = "[a-z A-Z]+", message = "invalide input. Name should contains only latin litters")
    private String name;
    @Column(name = "creation_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    //yyyy-mm-dd hh:mm:ss. instant 2022-03-20T10:11:12
    private Instant creationDate;

    public Wallet(String name) {
        this.name = name;
        this.creationDate = Instant.now();
    }

    public Timestamp convertInstantToTimestamp (Instant creationDate) {
        this.creationDate = creationDate;
        return Timestamp.from(this.creationDate);
    }
}