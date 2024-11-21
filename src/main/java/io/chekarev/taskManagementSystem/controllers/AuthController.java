package io.chekarev.taskManagementSystem.controllers;

import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.services.AuthService;
import io.chekarev.taskManagementSystem.services.UserService;
import io.chekarev.taskManagementSystem.domain.dto.*;
import io.chekarev.taskManagementSystem.exceptions.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;


/**
 * Контроллер для обработки запросов, связанных с пользователями.
 * Обеспечивает регистрацию, вход, выход, обновление и удаление профилей пользователей.
 */
@Tag(name = "AuthController", description = "Операции для авторизации пользователей, регистрации.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    /**
     * Регистрация нового пользователя.
     *
     * @param userDtoRegisterUpdate Данные для регистрации пользователя
     * @return Ответ с кодом состояния и сообщением
     */
    @Operation(summary = "Регистрация нового пользователя", description = "Регистрирует нового пользователя с переданными данными.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDtoRegisterUpdate userDtoRegisterUpdate) {
        try {
            authService.registerUser(userDtoRegisterUpdate);
        } catch (SQLException | UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Вход пользователя в систему.
     *
     * @param userDtoLogin Данные для входа (email и пароль)
     * @param currentUser текущий пользователь
     * @return Ответ с данными пользователя или сообщение об ошибке
     */
    @Operation(summary = "Авторизация пользователя", description = "Авторизирует пользователя с переданными данными.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDtoLogin userDtoLogin, @AuthenticationPrincipal User currentUser) {
        if (currentUser != null && currentUser.getEmail() != null && currentUser.getEmail().isEmpty()) {
            return ResponseEntity.status(403).body("You are already authorized");
        }

        try {
            return ResponseEntity.ok(authService.loginUser(userDtoLogin));
        } catch (SQLException | InvalidCredentialsException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    /**
     * Проверяет авторизацию пользователя
     *
     * @param currentUser текущий пользователь
     * @return Ответ с данными авторизированного пользователя
     */
    @Operation(summary = "Тест авторизации пользователя", description = "Проверяет авторизацию пользователя.")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test")
    public ResponseEntity<?> testAuth(@AuthenticationPrincipal User currentUser) {
        try {
            return ResponseEntity.ok().body(currentUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Проверяет пользователя на права админа
     *
     * @param currentUser текущий пользователь
     * @return Ответ с данными пользователя админа.
     */
    @Operation(summary = "Тест авторизации пользователя админа", description = "Проверяет авторизацию пользователя.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/admin/test")
    public ResponseEntity<?> testAdmin(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(currentUser);
    }
}
