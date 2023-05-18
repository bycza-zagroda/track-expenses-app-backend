package pl.byczazagroda.trackexpensesappbackend.dto;

import pl.byczazagroda.trackexpensesappbackend.model.UserStatus;

public record UserDTO (
        Long id, String user_name,  String email,
         String password, UserStatus userStatus){
}
