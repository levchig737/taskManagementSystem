package io.chekarev.taskManagementSystem.services;


import io.chekarev.taskManagementSystem.domain.dto.CommentDTO;

import java.util.List;

/**
 * Интерфейс сервиса для работы с комментариями. Предоставляет методы для получения, добавления
 * и удаления комментариев, а также для удаления комментариев текущим пользователем.
 */
public interface CommentService {

    /**
     * Получить список комментариев для задачи по ID.
     *
     * @param taskId ID задачи.
     * @return Список комментариев для указанной задачи.
     */
    List<CommentDTO> getCommentsByTaskId(Long taskId);

    /**
     * Добавить новый комментарий.
     *
     * @param commentDTO Данные для добавления нового комментария.
     * @return Добавленный комментарий.
     */
    CommentDTO addComment(CommentDTO commentDTO);

    /**
     * Удалить комментарий по ID.
     *
     * @param id ID комментария.
     */
    void deleteComment(Long id);

    /**
     * Удалить комментарий по ID, если он был оставлен текущим пользователем.
     *
     * @param id ID комментария.
     */
    void deleteCommentForCurrentUser(Long id);
}
