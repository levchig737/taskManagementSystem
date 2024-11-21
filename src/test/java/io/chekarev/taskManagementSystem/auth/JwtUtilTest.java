package io.chekarev.taskManagementSystem.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
//@TestPropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class JwtUtilTest {

    private static final String SECRET = "MySuperSecretKeyThatIsAtLeast32Characters";
    private static final int EXPIRATION_TIME = 60000; // 1 минута для тестирования
    private MACVerifier verifier;
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() throws JOSEException {
        jwtUtil = new JwtUtil(SECRET, EXPIRATION_TIME);
        byte[] secret = SECRET.getBytes(); // Преобразование секретного ключа в массив байтов
        this.verifier = new MACVerifier(secret); // Инициализация объекта MACVerifier с использованием секрета
    }

    @Test
    @DisplayName("[generate] Генерация токена должна быть успешной")
    public void shouldGenerateTokenSuccessfully() {
        String email = "test@example.com";

        String token = jwtUtil.generate(email);

        assertThat(token).isNotNull();
        assertThat(jwtUtil.verifyAndGetEmail(token)).isEqualTo(email);
    }

    @Test
    @DisplayName("[verifyAndGetUserId] Верификация должна вернуть email для корректного токена")
    public void shouldVerifyAndReturnEmail() {
        String email = "test@example.com";
        String token = jwtUtil.generate(email);

        String resultEmail = jwtUtil.verifyAndGetEmail(token);

        assertThat(resultEmail).isEqualTo(email);
    }

    @Test
    @DisplayName("[verifyAndGetUserId] Верификация должна вернуть null для истекшего токена")
    public String verifyAndGetEmail(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(verifier)) {
                return null; // Возвращаем null для неверной подписи
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime.before(new Date())) {
                return null; // Возвращаем null для истекшего токена
            }

            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            return null; // Возвращаем null для любых ошибок
        }
    }

}
