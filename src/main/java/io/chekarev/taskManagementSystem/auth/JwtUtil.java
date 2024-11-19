package io.chekarev.taskManagementSystem.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Утилита для работы с JWT-токенами.
 * Этот компонент генерирует и проверяет JWT-токены с использованием HMAC-шифрования.
 * Также предоставляет методы для извлечения и верификации данных из токенов.
 */
@Component
public class JwtUtil {

    private final JWSAlgorithm algorithm = JWSAlgorithm.HS256;
    private final JWSSigner signer;
    private final JWSVerifier verifier;
    private final int expirationTimeInMilliseconds;

    /**
     * Конструктор для инициализации JwtUtil с использованием секрета и времени жизни токена.
     *
     * @param jwtSecret Секретный ключ для подписания токенов.
     * @param expirationTimeInMilliseconds Время жизни токена в миллисекундах.
     * @throws JOSEException Если возникла ошибка при настройке криптографических компонентов.
     */
    public JwtUtil(@Value("${spring.security.jwt.secret}") String jwtSecret,
                   @Value("${spring.security.jwt.expirationTimeInMilliseconds}") int expirationTimeInMilliseconds) throws JOSEException {
        // Создание HMAC-ключа
        byte[] secret = jwtSecret.getBytes();
        this.signer = new MACSigner(secret);
        this.verifier = new MACVerifier(secret);
        this.expirationTimeInMilliseconds = expirationTimeInMilliseconds;
    }

    /**
     * Генерация JWT-токена для пользователя по email.
     * Токен включает информацию о пользователе, времени выпуска, времени истечения и уникальном идентификаторе.
     *
     * @param email Адрес электронной почты пользователя.
     * @return Сгенерированный JWT-токен.
     */
    public String generate(String email) {
        Instant now = Instant.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("Task Management System")
                .subject(email)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusMillis(expirationTimeInMilliseconds)))
                .jwtID(UUID.randomUUID().toString())
                .build();

        try {
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(algorithm),
                    claimsSet
            );
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to sign the JWT", e);
        }
    }

    /**
     * Проверка JWT-токена и извлечение адреса электронной почты пользователя.
     * <p>
     * Проверяет подпись токена, его срок действия и извлекает информацию о пользователе из токена.
     *
     * @param token JWT-токен.
     * @return Адрес электронной почты пользователя, если токен действителен.
     * @throws RuntimeException Если токен невалиден или истек.
     */
    public String verifyAndGetEmail(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid JWT signature");
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime.before(new Date())) {
                throw new RuntimeException("JWT token has expired");
            }

            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify or parse JWT", e);
        }
    }
}
