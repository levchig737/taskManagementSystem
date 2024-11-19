package io.chekarev.taskManagementSystem.controllers;

import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 * Этот контроллер обрабатывает HTTP-запросы для получения, обновления и удаления данных пользователей.
 * Включает в себя эндпоинты для работы с пользователями, используя сервисный слой.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    /**
     * Получение списка всех пользователей.
     * Возвращает список всех пользователей из базы данных.
     *
     * @return ResponseEntity с кодом состояния 200 (OK) и списком пользователей.
     */
    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей из базы данных.")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Получение пользователя по его идентификатору.
     * Возвращает информацию о пользователе с заданным идентификатором.
     *
     * @param id идентификатор пользователя.
     * @return ResponseEntity с кодом состояния 200 (OK) и объектом пользователя.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя по заданному идентификатору.")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Обновление данных пользователя.
     * Обновляет информацию о пользователе по его идентификатору.
     *
     * @param id          идентификатор пользователя, данные которого нужно обновить.
     * @param userDetails объект, содержащий новые данные для обновления.
     * @return ResponseEntity с кодом состояния 200 (OK) и обновленным пользователем.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя по его идентификатору.")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Удаление пользователя по его идентификатору.
     * Удаляет пользователя с заданным идентификатором из базы данных.
     *
     * @param id идентификатор пользователя, которого нужно удалить.
     * @return ResponseEntity с кодом состояния 204 (NO CONTENT) при успешном удалении.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из базы данных по заданному идентификатору.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
