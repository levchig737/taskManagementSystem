package io.chekarev.taskManagementSystem.services.impl;

import io.chekarev.taskManagementSystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Реализация службы для загрузки данных пользователя по его email.
 * Используется для аутентификации пользователей в системе безопасности Spring.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserService userService;

    /**
     * Загрузка данных пользователя по его email.
     * Используется для выполнения аутентификации через Spring Security.
     *
     * @param email email пользователя, данные которого нужно загрузить.
     * @return объект UserDetails с информацией о пользователе.
     * @throws UsernameNotFoundException если пользователь с данным email не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return userService.findByEmailForAuthentication(email);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found with email: " + email, e);
        }
    }
}
