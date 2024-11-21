package io.chekarev.taskManagementSystem.services;

import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.exceptions.ResourceNotFoundException;

import java.util.List;

/**
 * Сервис для управления пользователями.
 * Предоставляет методы для работы с данными пользователей.
 */
public interface UserService {

    /**
     * Получение списка всех пользователей.
     *
     * @return список всех пользователей.
     */
    List<User> getAllUsers();

    /**
     * Получение пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект пользователя.
     */
    User getUserById(Long id);

    /**
     * Обновление данных пользователя.
     *
     * @param id   идентификатор пользователя, данные которого нужно обновить.
     * @param user объект с новыми данными пользователя.
     * @return обновленный объект пользователя.
     */
    User updateUser(Long id, User user);

    /**
     * Удаление пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     */
    void deleteUser(Long id);

    /**
     * Поиск пользователя по email для аутентификации.
     *
     * @param email email пользователя.
     * @return объект пользователя.
     */
    User findByEmailForAuthentication(String email);

    /**
     * Получение текущего аутентифицированного пользователя.
     *
     * @return текущий пользователь.
     * @throws ResourceNotFoundException если пользователь не найден.
     */
    User getCurrentUser();

    /**
     * Обновление данных текущего аутентифицированного пользователя.
     *
     * @param userDetails объект с новыми данными пользователя.
     * @return обновленный пользователь.
     */
    User updateCurrentUser(User userDetails);
}
