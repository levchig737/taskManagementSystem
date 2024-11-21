package io.chekarev.taskManagementSystem.services.impl;

import io.chekarev.taskManagementSystem.auth.JwtUtil;
import io.chekarev.taskManagementSystem.domain.dto.UserDtoLogin;
import io.chekarev.taskManagementSystem.domain.dto.UserDtoRegisterUpdate;
import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.domain.mappers.UserMapper;
import io.chekarev.taskManagementSystem.exceptions.InvalidCredentialsException;
import io.chekarev.taskManagementSystem.exceptions.UserAlreadyExistsException;
import io.chekarev.taskManagementSystem.repositories.UserRepository;
import io.chekarev.taskManagementSystem.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Сервис для аутентификации пользователей.
 * Содержит бизнес-логику для регистрации, аутентификации пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    /**
     * Регистрация нового пользователя.
     *
     * @param userDtoRegisterUpdate Данные для регистрации пользователя
     * @throws UserAlreadyExistsException Если пользователь с таким email уже существует
     * @throws SQLException В случае ошибок при работе с базой данных
     */
    @Override
    public void registerUser(UserDtoRegisterUpdate userDtoRegisterUpdate) throws UserAlreadyExistsException, SQLException {
        if (userRepository.findByEmail(userDtoRegisterUpdate.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists.");
        }
        User user = userMapper.userDtoRegisterUpdateToUser(userDtoRegisterUpdate);

        userRepository.save(user);
    }

    /**
     * Вход пользователя в систему.
     *
     * @param userDtoLogin Данные для входа (email и пароль)
     * @return Данные пользователя после успешного входа
     * @throws SQLException В случае ошибок при работе с базой данных
     * @throws InvalidCredentialsException Если указаны неверные учетные данные
     */
    @Override
    public String loginUser(UserDtoLogin userDtoLogin) throws SQLException, InvalidCredentialsException {
        Optional<User> user = userRepository.findByEmail(userDtoLogin.getEmail());
        if (user.isPresent() && user.get().getPassword().equals(userDtoLogin.getPassword())) {
            return jwtUtil.generate(user.get().getEmail());
        }
        throw new InvalidCredentialsException("Invalid email or password.");
    }
}
