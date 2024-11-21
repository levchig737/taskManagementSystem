package io.chekarev.taskManagementSystem.services.impl;

import io.chekarev.taskManagementSystem.domain.dto.TaskDTO;
import io.chekarev.taskManagementSystem.domain.entities.Task;
import io.chekarev.taskManagementSystem.domain.mappers.TaskMapper;
import io.chekarev.taskManagementSystem.exceptions.ResourceNotFoundException;
import io.chekarev.taskManagementSystem.repositories.TaskRepository;
import io.chekarev.taskManagementSystem.repositories.specifications.TaskSpecification;
import io.chekarev.taskManagementSystem.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с задачами. Предоставляет методы для получения списка задач,
 * создания, обновления и удаления задач, а также для получения задач для текущего пользователя.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Получить все задачи с учетом фильтрации по статусу, приоритету, автору и исполнителю,
     * а также с применением пагинации.
     *
     * @param pageable Параметры пагинации.
     * @param status Статус задачи (может быть null или пустым).
     * @param priority Приоритет задачи (может быть null или пустым).
     * @param author ID автора задачи (может быть null или пустым).
     * @param assignee ID исполнителя задачи (может быть null или пустым).
     * @return Страница задач, соответствующих фильтрам.
     */
    @Override
    public Page<TaskDTO> getAllTasks(Pageable pageable, String status, String priority, String author, String assignee) {
        return taskRepository.findAll(
                TaskSpecification.withFilters(status, priority, author, assignee),
                pageable
        ).map(taskMapper::toDto);
    }

    /**
     * Получить задачу по ее ID.
     *
     * @param id ID задачи.
     * @return Задача с заданным ID в виде DTO.
     * @throws ResourceNotFoundException Если задача с данным ID не найдена.
     */
    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.toDto(task);
    }

    /**
     * Создать новую задачу.
     *
     * @param taskDTO Данные для создания новой задачи.
     * @return Созданная задача в виде DTO.
     */
    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    /**
     * Обновить задачу с указанным ID.
     *
     * @param id ID задачи, которую необходимо обновить.
     * @param taskDTO Данные для обновления задачи.
     * @return Обновленная задача в виде DTO.
     * @throws ResourceNotFoundException Если задача с данным ID не найдена.
     */
    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    /**
     * Удалить задачу по ее ID.
     *
     * @param id ID задачи, которую необходимо удалить.
     * @throws ResourceNotFoundException Если задача с данным ID не найдена.
     */
    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskRepository.delete(task);
    }

    /**
     * Получить задачи для текущего пользователя, включая задачи, где он является автором
     * и задач, где он назначен исполнителем, с применением пагинации.
     *
     * @param userId ID текущего пользователя.
     * @param pageable Параметры пагинации.
     * @return Список задач, относящихся к текущему пользователю.
     */
    @Override
    public List<TaskDTO> getTasksForCurrentUser(Long userId, Pageable pageable) {
        // Получаем задачи, где пользователь является автором
        List<Task> authoredTasks = taskRepository.findByAuthorId(userId);

        // Получаем задачи, где пользователь является исполнителем
        List<Task> assignedTasks = taskRepository.findByAssigneeId(userId);

        // Объединяем оба списка и преобразуем их в DTO
        List<TaskDTO> result = authoredTasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        result.addAll(assignedTasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList()));

        // Применяем пагинацию вручную
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), result.size());

        if (start > result.size()) {
            return List.of(); // Если начальный индекс выходит за пределы, возвращаем пустой список
        }

        return result.subList(start, end);
    }
}
