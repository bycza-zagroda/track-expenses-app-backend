package pl.byczazagroda.trackexpensesappbackend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;

public record CreateWalletDTO(@NotBlank @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+") String name, Instant creationTime) {
}
