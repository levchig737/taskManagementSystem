package io.chekarev.taskManagementSystem.services.impl;

import io.chekarev.taskManagementSystem.domain.dto.CommentDTO;
import io.chekarev.taskManagementSystem.domain.entities.Comment;
import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.domain.mappers.CommentMapper;
import io.chekarev.taskManagementSystem.exceptions.ResourceNotFoundException;
import io.chekarev.taskManagementSystem.repositories.CommentRepository;
import io.chekarev.taskManagementSystem.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с комментариями. Осуществляет логику получения, добавления,
 * удаления комментариев и проверки прав текущего пользователя на удаление своих комментариев.
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    /**
     * Получить список комментариев для задачи по ее ID.
     *
     * @param taskId ID задачи.
     * @return Список комментариев для указанной задачи.
     */
    @Override
    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Добавить новый комментарий в систему.
     *
     * @param commentDTO Данные для добавления нового комментария.
     * @return Добавленный комментарий.
     */
    @Override
    public CommentDTO addComment(CommentDTO commentDTO) {
        Comment comment = commentMapper.toEntity(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    /**
     * Удалить комментарий по ID.
     *
     * @param id ID комментария.
     * @throws ResourceNotFoundException Если комментарий с данным ID не найден.
     */
    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        commentRepository.delete(comment);
    }

    /**
     * Удалить комментарий, если он был оставлен текущим пользователем.
     *
     * @param id ID комментария.
     * @throws ResourceNotFoundException Если комментарий не найден или если текущий пользователь не является его владельцем.
     */
    @Override
    public void deleteCommentForCurrentUser(Long id) {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        // Проверка, что текущий пользователь является владельцем комментария
        if (!comment.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new ResourceNotFoundException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }
}
