package pl.byczazagroda.trackexpensesappbackend.model.dto;

import lombok.Builder;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Column;

@Builder
public class WalletDto {

  @Column(nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @NonNull
  private UUID walletId;

  @NonNull
  private String name;
}
