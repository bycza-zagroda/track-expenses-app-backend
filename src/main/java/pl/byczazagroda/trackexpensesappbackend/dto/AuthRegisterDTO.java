package pl.byczazagroda.trackexpensesappbackend.dto;

import org.springframework.beans.factory.annotation.Value;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record AuthRegisterDTO(@NotBlank @Value("${regex.pattern.email}") String email,
                              @NotBlank @Value("${regex.pattern.password}") String password,
                              @NotBlank @Size(max = 20) String username) { }
