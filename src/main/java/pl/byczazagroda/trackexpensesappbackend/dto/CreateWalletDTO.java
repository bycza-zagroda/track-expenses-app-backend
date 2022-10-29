package pl.byczazagroda.trackexpensesappbackend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;

public record CreateWalletDTO(@NotBlank @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+") String name, LocalDateTime creationDate) {
}
