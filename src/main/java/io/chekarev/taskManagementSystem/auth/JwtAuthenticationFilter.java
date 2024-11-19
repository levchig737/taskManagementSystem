package io.chekarev.taskManagementSystem.auth;

import io.chekarev.taskManagementSystem.services.impl.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для аутентификации с использованием JWT.
 * Этот фильтр извлекает токен из заголовка Authorization, проверяет его валидность
 * и загружает пользователя, если токен действителен, устанавливая аутентификацию в контексте безопасности.
 */
@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Метод для обработки каждого HTTP-запроса.
     * Проверяет наличие и валидность JWT в заголовке Authorization.
     * Если токен действителен, загружает данные пользователя и устанавливает аутентификацию.
     *
     * @param request  HTTP-запрос.
     * @param response HTTP-ответ.
     * @param filterChain Цепочка фильтров.
     * @throws ServletException Если произошла ошибка сервлета.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // Верификация токена и извлечение email
                String email = jwtUtil.verifyAndGetEmail(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Загрузка данных пользователя
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    // Проверка дополнительных данных безопасности (при необходимости)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                log.error("JWT authentication failed: {}", e.getMessage());
                SecurityContextHolder.clearContext(); // Очищаем контекст безопасности
            }
        }

        filterChain.doFilter(request, response);
    }
}
