package io.chekarev.taskManagementSystem.controllers;

import io.chekarev.taskManagementSystem.domain.dto.CommentDTO;
import io.chekarev.taskManagementSystem.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления комментариями. Предоставляет API для администраторов и текущих пользователей
 * для выполнения операций с комментариями, включая создание, удаление и получение комментариев.
 */
@Tag(name = "CommentController", description = "API для управления комментариями")
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * Получить комментарии по ID задачи для администратора.
     *
     * @param taskId ID задачи.
     * @return Список комментариев для указанной задачи.
     */
    @Operation(summary = "Получить комментарии по ID задачи", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/admin/task/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskIdAdmin(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId));
    }

    /**
     * Удалить комментарий по ID для администратора.
     *
     * @param id ID комментария.
     * @return Ответ без содержимого.
     */
    @Operation(summary = "Удалить комментарий по ID", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteCommentAdmin(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить комментарии по ID задачи для текущего пользователя.
     *
     * @param taskId ID задачи.
     * @return Список комментариев для указанной задачи.
     */
    @Operation(summary = "Получить комментарии по ID задачи для текущего пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskIdForCurrentUser(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId));
    }

    /**
     * Добавить новый комментарий.
     *
     * @param commentDTO Данные для добавления нового комментария.
     * @return Добавленный комментарий.
     */
    @Operation(summary = "Добавить новый комментарий", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.addComment(commentDTO));
    }

    /**
     * Удалить комментарий по ID для текущего пользователя.
     *
     * @param id ID комментария.
     * @return Ответ без содержимого.
     */
    @Operation(summary = "Удалить комментарий по ID для текущего пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> deleteCommentForCurrentUser(@PathVariable Long id) {
        commentService.deleteCommentForCurrentUser(id);
        return ResponseEntity.noContent().build();
    }
}
