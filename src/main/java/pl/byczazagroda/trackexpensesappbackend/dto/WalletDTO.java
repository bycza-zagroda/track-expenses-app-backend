package pl.byczazagroda.trackexpensesappbackend.dto;

import java.time.Instant;

/**
 *
 * @param id is a technical identity number
 * @param name is a Wallet's name
 * @param creationDate is a date and hour when wallet has been created
 */
public record WalletDTO(Long id, String name, Instant creationDate) {

}
