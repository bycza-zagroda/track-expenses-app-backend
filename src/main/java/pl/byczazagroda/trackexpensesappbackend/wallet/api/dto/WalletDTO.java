package pl.byczazagroda.trackexpensesappbackend.wallet.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Wallet Data Transfer Object class.
 *
 * @param id is a technical identity number
 * @param name is a Wallet's name
 * @param creationDate is a date and hour when wallet has been created
 */
public record WalletDTO(Long id, String name, Instant creationDate, Long userId, BigDecimal balance) {

    public WalletDTO(Long id, String name, Instant creationDate, Long userId) {
        this(id, name, creationDate, userId, BigDecimal.ZERO);
    }

}
