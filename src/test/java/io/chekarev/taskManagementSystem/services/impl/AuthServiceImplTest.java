package io.chekarev.taskManagementSystem.services.impl;

import io.chekarev.taskManagementSystem.auth.JwtUtil;
import io.chekarev.taskManagementSystem.domain.dto.UserDtoLogin;
import io.chekarev.taskManagementSystem.domain.dto.UserDtoRegisterUpdate;
import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.domain.mappers.UserMapper;
import io.chekarev.taskManagementSystem.exceptions.*;
import io.chekarev.taskManagementSystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserDtoRegisterUpdate userDtoRegisterUpdate;
    private UserDtoLogin userDtoLogin;

    @BeforeEach
    void setUp() {
        userDtoRegisterUpdate = new UserDtoRegisterUpdate("test@example.com", "Test User", "password");
        userDtoLogin = new UserDtoLogin("test@example.com", "password");
    }

    @Test
    @DisplayName("[registerUser_success] Регистрирует пользователя успешно")
    void registerUser_success() throws SQLException, UserAlreadyExistsException {
        User userEntity = new User();
        when(userRepository.findByEmail(userDtoRegisterUpdate.getEmail())).thenReturn(Optional.empty());
        when(userMapper.userDtoRegisterUpdateToUser(userDtoRegisterUpdate)).thenReturn(userEntity);

        authService.registerUser(userDtoRegisterUpdate);

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    @DisplayName("[registerUser_userAlreadyExists] Ошибка регистрации: пользователь с таким email уже существует")
    void registerUser_userAlreadyExists() throws SQLException {
        when(userRepository.findByEmail(userDtoRegisterUpdate.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser(userDtoRegisterUpdate));
    }

    @Test
    @DisplayName("[loginUser_success] Успешный вход пользователя")
    void loginUser_success() throws SQLException, InvalidCredentialsException {
        User userEntity = new User();
        userEntity.setPassword("password");
        userEntity.setId(1L);
        when(userRepository.findByEmail(userDtoLogin.getEmail())).thenReturn(Optional.of(userEntity));
        when(jwtUtil.generate(userEntity.getEmail())).thenReturn("mocked_jwt_token");

        String token = authService.loginUser(userDtoLogin);

        assertEquals("mocked_jwt_token", token);
    }

    @Test
    @DisplayName("[loginUser_invalidCredentials] Ошибка входа: неверный пароль")
    void loginUser_invalidCredentials() throws SQLException {
        User userEntity = new User();
        userEntity.setPassword("correct_password");
        when(userRepository.findByEmail(userDtoLogin.getEmail())).thenReturn(Optional.of(userEntity));

        assertThrows(InvalidCredentialsException.class, () -> authService.loginUser(userDtoLogin));
    }

    @Test
    @DisplayName("[loginUser_userNotFound] Ошибка входа: пользователь не найден")
    void loginUser_userNotFound() throws SQLException {
        when(userRepository.findByEmail(userDtoLogin.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.loginUser(userDtoLogin));
    }
}
