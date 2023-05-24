package pl.byczazagroda.trackexpensesappbackend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    /**
     * Class version 0.5.0.  SerialVersionUID needs to be updated with any change.
     */
    @Serial
    private static final long serialVersionUID = 10050L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Wallet> wallets = new ArrayList<>();

    @NotBlank
    @Size(min = 1, max = 50)
    private String userName;

    @NotBlank
    @Size(min = 6, max = 120)
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+.)+[\\w-]{2,4}$")
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", columnDefinition = "ENUM('VERIFIED', 'UNVERIFIED', 'BLOCKED', 'BANNED')")
    private UserStatus userStatus;

    public void addWallet(Wallet wallet) {
        this.wallets.add(wallet);
        wallet.setUser(this);
    }

    public void removeWallet(Wallet wallet) {
        this.wallets.remove(wallet);
        wallet.setUser(null);
    }
}
