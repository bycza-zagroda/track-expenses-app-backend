package pl.byczazagroda.trackexpensesappbackend.service;

import pl.byczazagroda.trackexpensesappbackend.dto.AuthAccessTokenDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthLoginDTO;

public interface UserService {
    AuthAccessTokenDTO authenticateUser(AuthLoginDTO authLoginDTO);
}
