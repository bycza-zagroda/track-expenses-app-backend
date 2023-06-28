package pl.byczazagroda.trackexpensesappbackend.service;


import pl.byczazagroda.trackexpensesappbackend.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

public interface UserService {

    void registerUser(@Valid AuthRegisterDTO authRegisterDTO);

    String hashPassword(String password);

    String loginUser(AuthLoginDTO authLoginDTO, HttpServletResponse response);
}
