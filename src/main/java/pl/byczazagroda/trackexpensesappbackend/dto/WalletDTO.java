package pl.byczazagroda.trackexpensesappbackend.dto;

import java.time.Instant;


public record WalletDTO(Long id, String name, Instant creationDate) {

}
