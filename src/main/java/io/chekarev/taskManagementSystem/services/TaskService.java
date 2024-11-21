package io.chekarev.taskManagementSystem.services;

import io.chekarev.taskManagementSystem.domain.dto.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Интерфейс для сервиса работы с задачами. Предоставляет методы для получения, создания, обновления
 * и удаления задач, а также для получения задач для текущего пользователя.
 */
public interface TaskService {

    /**
     * Получить все задачи с учетом фильтрации и пагинации.
     *
     * @param pageable Параметры пагинации.
     * @param status Статус задачи (может быть null или пустым).
     * @param priority Приоритет задачи (может быть null или пустым).
     * @param author ID автора задачи (может быть null или пустым).
     * @param assignee ID исполнителя задачи (может быть null или пустым).
     * @return Страница задач, соответствующих фильтрам.
     */
    Page<TaskDTO> getAllTasks(Pageable pageable, String status, String priority, String author, String assignee);

    /**
     * Получить задачу по ID.
     *
     * @param id ID задачи.
     * @return Задача с заданным ID.
     */
    TaskDTO getTaskById(Long id);

    /**
     * Создать новую задачу.
     *
     * @param taskDTO Данные для создания новой задачи.
     * @return Созданная задача.
     */
    TaskDTO createTask(TaskDTO taskDTO);

    /**
     * Обновить задачу по ID.
     *
     * @param id ID задачи, которую необходимо обновить.
     * @param taskDTO Данные для обновления задачи.
     * @return Обновленная задача.
     */
    TaskDTO updateTask(Long id, TaskDTO taskDTO);

    /**
     * Удалить задачу по ID.
     *
     * @param id ID задачи, которую необходимо удалить.
     */
    void deleteTask(Long id);

    /**
     * Получить задачи для текущего пользователя по его ID с учетом пагинации.
     *
     * @param userId ID пользователя.
     * @param pageable Параметры пагинации.
     * @return Список задач для текущего пользователя.
     */
    List<TaskDTO> getTasksForCurrentUser(Long userId, Pageable pageable);
}

