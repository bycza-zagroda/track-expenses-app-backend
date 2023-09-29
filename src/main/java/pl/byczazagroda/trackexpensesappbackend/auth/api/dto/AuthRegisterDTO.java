package pl.byczazagroda.trackexpensesappbackend.auth.api.dto;

import pl.byczazagroda.trackexpensesappbackend.general.regex.RegexConstant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record AuthRegisterDTO(@NotBlank @Pattern(regexp = RegexConstant.EMAIL_PATTERN) String email,
                              @NotBlank @Pattern(regexp = RegexConstant.PASSWORD_PATTERN) String password,
                              @NotBlank @Size(max = 20) String username) { }
