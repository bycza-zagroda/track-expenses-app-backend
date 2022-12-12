package pl.byczazagroda.trackexpensesappbackend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record CreateWalletDTO(@NotBlank @NotEmpty @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+", message = "DUPCIA") String name) {
}
