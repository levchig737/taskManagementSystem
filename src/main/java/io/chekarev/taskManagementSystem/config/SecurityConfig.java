package io.chekarev.taskManagementSystem.config;

import io.chekarev.taskManagementSystem.auth.JwtAuthenticationFilter;
import io.chekarev.taskManagementSystem.config.handlers.CustomAccessDeniedHandler;
import io.chekarev.taskManagementSystem.config.handlers.CustomAuthenticationEntryPoint;
import io.chekarev.taskManagementSystem.services.impl.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности приложения.
 * Этот класс настраивает фильтры безопасности, разрешения на доступ к эндпоинтам, а также обработчики исключений.
 * Включает в себя настройку аутентификации, паролей и защиты от CSRF.
 */
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * Конфигурация фильтра безопасности, которая настраивает доступ к различным эндпоинтам
     * в зависимости от роли пользователя и регистрирует фильтр JWT для аутентификации.
     *
     * @param http объект конфигурации HTTP-безопасности.
     * @return настроенный объект SecurityFilterChain.
     * @throws Exception если произошла ошибка конфигурации безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Отключаем CSRF-защиту
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("users/admin/**", "comments/admin/**", "tasks/admin/**", "auth/admin/**").hasAuthority("ADMIN") // Доступ для администраторов
                        .requestMatchers("/auth/**").permitAll() // Открытый доступ к эндпоинтам аутентификации
                        .requestMatchers("/users/**").permitAll() // Открытый доступ к пользователям
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll() // Открытый доступ к Swagger UI
                        .anyRequest().authenticated()  // Для остальных запросов требуется аутентификация
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler) // Обработчик доступа
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Обработчик ошибок аутентификации
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Добавление фильтра для аутентификации через JWT

        return http.build();
    }

    /**
     * Бин для создания кодировщика паролей с использованием алгоритма BCrypt.
     *
     * @return PasswordEncoder для BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создание AuthenticationManager с использованием настраиваемого DaoAuthenticationProvider.
     * Конфигурирует аутентификацию через базу данных с использованием userDetailsService и passwordEncoder.
     *
     * @param http объект конфигурации HTTP-безопасности.
     * @param passwordEncoder кодировщик паролей.
     * @param userDetailsService сервис для получения данных о пользователе.
     * @return настроенный AuthenticationManager.
     * @throws Exception если произошла ошибка конфигурации аутентификации.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider(passwordEncoder, userDetailsService)) // Использует DAO-поставщика для аутентификации
                .build();
    }

    /**
     * Создание DaoAuthenticationProvider, который используется для аутентификации пользователей через базу данных.
     * Этот провайдер настраивает использование userDetailsService для получения данных о пользователе и passwordEncoder для проверки паролей.
     *
     * @param passwordEncoder кодировщик паролей.
     * @param userDetailsService сервис для загрузки пользователя.
     * @return настроенный DaoAuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}
