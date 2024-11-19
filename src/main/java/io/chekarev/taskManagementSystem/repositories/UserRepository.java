package io.chekarev.taskManagementSystem.repositories;

import io.chekarev.taskManagementSystem.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью пользователей.
 * Обеспечивает доступ к данным пользователей в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Поиск пользователя по его email.
     *
     * @param email email пользователя.
     * @return Optional, содержащий объект пользователя, если найден.
     */
    Optional<User> findByEmail(String email);
}
