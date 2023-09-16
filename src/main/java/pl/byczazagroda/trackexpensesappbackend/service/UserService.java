package pl.byczazagroda.trackexpensesappbackend.service;


import pl.byczazagroda.trackexpensesappbackend.dto.AuthLoginDTO;
import pl.byczazagroda.trackexpensesappbackend.dto.AuthRegisterDTO;
import pl.byczazagroda.trackexpensesappbackend.financialTransaction.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

public interface UserService {

    void registerUser(@Valid AuthRegisterDTO authRegisterDTO);

    String hashPassword(String password);

    String loginUser(AuthLoginDTO authLoginDTO, HttpServletResponse response);

    User getUserFromToken(String token);

    String createAccessToken(User user);

    Cookie createRefreshTokenCookie(User user);

    String refreshToken(HttpServletRequest request, HttpServletResponse response,
                        String refreshToken, String accessToken);

    void deleteRefreshTokenCookie(HttpServletResponse response);
}
