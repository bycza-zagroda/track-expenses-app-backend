package pl.byczazagroda.trackexpensesappbackend.dto;

import javax.validation.constraints.NotBlank;

public record AuthAccessTokenDTO(@NotBlank String accessToken) {}
