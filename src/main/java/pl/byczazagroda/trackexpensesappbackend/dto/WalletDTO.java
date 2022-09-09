package pl.byczazagroda.trackexpensesappbackend.dto;

import org.hibernate.annotations.Immutable;

import java.time.Instant;


@Immutable
public record WalletDTO(Long id, String name, Instant creationDate) {

}
