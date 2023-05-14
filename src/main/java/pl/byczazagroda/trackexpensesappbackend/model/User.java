package pl.byczazagroda.trackexpensesappbackend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    @OneToMany
    @JoinColumn(name = "userId")
    private Long walletId;

    @NotBlank
    @Size(min = 1, max = 50)
    private String userName;

    @NotBlank
    @Size(min = 11, max = 120)
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}
