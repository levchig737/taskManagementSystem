package io.chekarev.taskManagementSystem.controllers;

import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями. Предоставляет API для администраторов и текущих пользователей
 * для выполнения операций с пользователями, включая создание, обновление, удаление и получение информации.
 */
@Tag(name = "UserController", description = "API для управления пользователями")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Получить список всех пользователей. Этот эндпоинт доступен только администраторам.
     *
     * @return Список всех пользователей.
     */
    @Operation(summary = "Получить список всех пользователей", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/admin")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Получить данные пользователя по ID. Этот эндпоинт доступен только администраторам.
     *
     * @param id ID пользователя.
     * @return Данные пользователя.
     */
    @Operation(summary = "Получить данные пользователя по ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/admin/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Обновить данные пользователя. Этот эндпоинт доступен только администраторам.
     *
     * @param id ID пользователя.
     * @param user Обновленные данные пользователя.
     * @return Обновленные данные пользователя.
     */
    @Operation(summary = "Обновить данные пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/admin/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    /**
     * Удалить пользователя по ID. Этот эндпоинт доступен только администраторам.
     *
     * @param id ID пользователя.
     * @return Ответ без содержимого.
     */
    @Operation(summary = "Удалить пользователя по ID", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить данные текущего пользователя.
     *
     * @return Данные текущего пользователя.
     */
    @Operation(summary = "Получить данные текущего пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    /**
     * Обновить данные текущего пользователя.
     *
     * @param user Обновленные данные текущего пользователя.
     * @return Обновленные данные пользователя.
     */
    @Operation(summary = "Обновить данные текущего пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/me")
    public ResponseEntity<User> updateCurrentUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateCurrentUser(user));
    }
}

