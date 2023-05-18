package pl.byczazagroda.trackexpensesappbackend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record AuthLoginDTO(@NotBlank @Pattern(regexp = "^[\\w-.]+@([\\w-]+.)+[\\w-]{2,4}$") String email,
                           @NotBlank @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])" +
                                                       "[A-Za-z\\d@$!%*?&]{8,}$") @Size(min = 8) String password,
                           @NotNull Boolean isRememberMe) {}
