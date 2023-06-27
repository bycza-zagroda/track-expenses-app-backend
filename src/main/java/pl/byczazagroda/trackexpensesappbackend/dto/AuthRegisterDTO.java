package pl.byczazagroda.trackexpensesappbackend.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record AuthRegisterDTO(@NotBlank @Pattern(regexp = "^[\\w-.]+@([\\w-]+.)+[\\w-]{2,4}$") String email,
                              @NotBlank @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])"
                                      + "(?=.*?[@$!%?&]).{8,100}$") @Size(min = 8, max = 100) String password,
                              @NotBlank @Size(max = 20) String username) { }
