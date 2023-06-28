package pl.byczazagroda.trackexpensesappbackend.dto;

import org.springframework.beans.factory.annotation.Value;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record AuthLoginDTO(@NotBlank @Value("${regex.pattern.email}") String email,
                           @NotBlank @Value("${regex.pattern.password}")
                           String password, @NotNull Boolean isRememberMe) { }
