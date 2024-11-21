package io.chekarev.taskManagementSystem.controllers;

import io.chekarev.taskManagementSystem.domain.dto.TaskDTO;
import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления задачами. Предоставляет API для администраторов и текущих пользователей
 * для выполнения операций с задачами, включая создание, обновление, удаление и получение информации.
 */
@Tag(name = "TaskController", description = "API для управления задачами")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Получить список всех задач с фильтрацией и пагинацией. Этот эндпоинт доступен только администраторам.
     *
     * @param status Фильтр по статусу задачи.
     * @param priority Фильтр по приоритету задачи.
     * @param author Фильтр по автору задачи.
     * @param assignee Фильтр по исполнителю задачи.
     * @param pageable Параметры пагинации.
     * @return Список задач с учетом фильтров и пагинации.
     */
    @Operation(summary = "Получить список всех задач с фильтрацией и пагинацией", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/admin")
    public ResponseEntity<Page<TaskDTO>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String assignee,
            Pageable pageable
    ) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable, status, priority, author, assignee));
    }

    /**
     * Получить задачу по ID. Этот эндпоинт доступен только администраторам.
     *
     * @param id ID задачи.
     * @return Детали задачи.
     */
    @Operation(summary = "Получить задачу по ID (админ)", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/admin/{id}")
    public ResponseEntity<TaskDTO> getTaskByIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * Создать новую задачу. Этот эндпоинт доступен только администраторам.
     *
     * @param taskDTO Данные для создания задачи.
     * @return Созданная задача.
     */
    @Operation(summary = "Создать новую задачу (админ)", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/admin")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.createTask(taskDTO));
    }

    /**
     * Обновить задачу по ID. Этот эндпоинт доступен только администраторам.
     *
     * @param id ID задачи.
     * @param taskDTO Обновленные данные задачи.
     * @return Обновленная задача.
     */
    @Operation(summary = "Обновить задачу по ID (админ)", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/admin/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    /**
     * Удалить задачу по ID. Этот эндпоинт доступен только администраторам.
     *
     * @param id ID задачи.
     * @return Ответ без содержимого.
     */
    @Operation(summary = "Удалить задачу по ID (админ)", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить задачи текущего пользователя с пагинацией.
     *
     * @param currentUser Текущий аутентифицированный пользователь.
     * @param pageable Параметры пагинации.
     * @return Список задач текущего пользователя.
     */
    @Operation(summary = "Получить задачи текущего пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasksForCurrentUser(
            @AuthenticationPrincipal User currentUser,
            Pageable pageable
    ) {
        return ResponseEntity.ok(taskService.getTasksForCurrentUser(currentUser.getId(), pageable));
    }

    /**
     * Получить задачу текущего пользователя по ID.
     *
     * @param id ID задачи.
     * @return Детали задачи текущего пользователя.
     */
    @Operation(summary = "Получить задачу текущего пользователя по ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskByIdForCurrentUser(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * Обновить задачу текущего пользователя.
     *
     * @param id ID задачи.
     * @param taskDTO Обновленные данные задачи.
     * @return Обновленная задача.
     */
    @Operation(summary = "Обновить задачу текущего пользователя", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTaskForCurrentUser(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }
}
