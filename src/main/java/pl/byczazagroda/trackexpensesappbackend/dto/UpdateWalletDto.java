package pl.byczazagroda.trackexpensesappbackend.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record UpdateWalletDto(@NotNull @NotEmpty @Min(value = 1) Long id, @NotBlank @NotEmpty @Size(max = 20) @Pattern(regexp = "[a-z A-Z]+") String name) {
}
