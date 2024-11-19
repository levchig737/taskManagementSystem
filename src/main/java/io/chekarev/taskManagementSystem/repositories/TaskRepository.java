package io.chekarev.taskManagementSystem.repositories;

import io.chekarev.taskManagementSystem.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью заданий.
 * Обеспечивает доступ к данным заданий в базе данных.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Получение списка заданий, созданных автором.
     *
     * @param authorId идентификатор автора заданий.
     * @return список заданий, созданных указанным автором.
     */
    List<Task> findByAuthorId(Long authorId);

    /**
     * Получение списка заданий, назначенных исполнителю.
     *
     * @param assigneeId идентификатор исполнителя заданий.
     * @return список заданий, назначенных указанному исполнителю.
     */
    List<Task> findByAssigneeId(Long assigneeId);

    /**
     * Получение списка заданий с указанным статусом и приоритетом.
     *
     * @param status   статус заданий.
     * @param priority приоритет заданий.
     * @return список заданий, соответствующих указанным критериям.
     */
    List<Task> findByStatusAndPriority(String status, String priority);
}
