package pl.byczazagroda.trackexpensesappbackend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record WalletUpdateDTO(
        @NotBlank @Size(max = 20) @Pattern(regexp = "[\\w ]+") String name) {
}
