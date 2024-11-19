package io.chekarev.taskManagementSystem.repositories;

import io.chekarev.taskManagementSystem.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью комментариев.
 * Обеспечивает доступ к данным комментариев в базе данных.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Получение списка комментариев, связанных с заданием.
     *
     * @param taskId идентификатор задания, для которого нужно найти комментарии.
     * @return список комментариев, связанных с заданием.
     */
    List<Comment> findByTaskId(Long taskId);
}
