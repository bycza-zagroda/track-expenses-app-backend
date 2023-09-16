package pl.byczazagroda.trackexpensesappbackend.dto;

import lombok.Builder;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.model.UserStatus;

@Builder
public record UserDTO (Long id, String userName,  String email, String password, UserStatus userStatus) {
}
