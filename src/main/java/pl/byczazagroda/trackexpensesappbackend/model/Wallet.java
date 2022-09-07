package pl.byczazagroda.trackexpensesappbackend.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Data
@Table(name = "wallet")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(nullable = false)
  private UUID walletId;

  @ToString.Include
  @Column(nullable = false)
  private String walletName;

  @Column(nullable = false)
  private OffsetDateTime createdAt;

  public Wallet(UUID walletId, String walletName) {
    this.walletId = walletId;
    this.walletName = walletName;
  }
}
