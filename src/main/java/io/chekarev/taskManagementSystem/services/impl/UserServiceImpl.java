package io.chekarev.taskManagementSystem.services.impl;

import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.exceptions.ResourceNotFoundException;
import io.chekarev.taskManagementSystem.repositories.UserRepository;
import io.chekarev.taskManagementSystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация сервиса для управления данными пользователей.
 * Предоставляет методы для работы с пользователями, включая получение, обновление и удаление.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Получение списка всех пользователей.
     *
     * @return список всех пользователей в базе данных.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получение пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект пользователя.
     * @throws ResourceNotFoundException если пользователь с заданным идентификатором не найден.
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * Обновление данных пользователя.
     *
     * @param id          идентификатор пользователя, данные которого нужно обновить.
     * @param userDetails объект с новыми данными для обновления.
     * @return обновленный объект пользователя.
     */
    @Override
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    /**
     * Удаление пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя, которого нужно удалить.
     * @throws ResourceNotFoundException если пользователь с заданным идентификатором не найден.
     */
    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    /**
     * Поиск пользователя по email для аутентификации.
     *
     * @param email email пользователя, которого нужно найти.
     * @return объект пользователя.
     * @throws ResourceNotFoundException если пользователь с данным email не найден.
     */
    @Override
    public User findByEmailForAuthentication(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Получение текущего аутентифицированного пользователя.
     *
     * @return текущий пользователь.
     * @throws ResourceNotFoundException если пользователь не найден.
     */
    @Override
    public User getCurrentUser() {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userDetails.getUsername()));
    }

    /**
     * Обновление данных текущего аутентифицированного пользователя.
     *
     * @param userDetails объект с новыми данными пользователя.
     * @return обновленный пользователь.
     */
    @Override
    public User updateCurrentUser(User userDetails) {
        User currentUser = getCurrentUser();
        currentUser.setName(userDetails.getName());
        currentUser.setEmail(userDetails.getEmail());
        return userRepository.save(currentUser);
    }
}
