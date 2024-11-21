package io.chekarev.taskManagementSystem.services;


import io.chekarev.taskManagementSystem.domain.dto.UserDtoLogin;
import io.chekarev.taskManagementSystem.domain.dto.UserDtoRegisterUpdate;
import io.chekarev.taskManagementSystem.exceptions.InvalidCredentialsException;
import io.chekarev.taskManagementSystem.exceptions.UserAlreadyExistsException;

import java.sql.SQLException;

/**
 * Контракт сервиса аутентификации пользователей.
 * Определяет методы для регистрации и аутентификации пользователей.
 */
public interface AuthService {

    /**
     * Регистрация нового пользователя.
     *
     * @param userDtoRegisterUpdate Данные для регистрации пользователя
     * @throws UserAlreadyExistsException Если пользователь с таким email уже существует
     * @throws SQLException В случае ошибок при работе с базой данных
     */
    void registerUser(UserDtoRegisterUpdate userDtoRegisterUpdate) throws UserAlreadyExistsException, SQLException;

    /**
     * Вход пользователя в систему.
     *
     * @param userDtoLogin Данные для входа (email и пароль)
     * @return Токен JWT при успешной аутентификации
     * @throws SQLException В случае ошибок при работе с базой данных
     * @throws InvalidCredentialsException Если указаны неверные учетные данные
     */
    String loginUser(UserDtoLogin userDtoLogin) throws SQLException, InvalidCredentialsException;
}
