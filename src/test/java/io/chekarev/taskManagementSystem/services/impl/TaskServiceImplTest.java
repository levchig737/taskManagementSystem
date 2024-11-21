package io.chekarev.taskManagementSystem.services.impl;

import io.chekarev.taskManagementSystem.domain.dto.TaskDTO;
import io.chekarev.taskManagementSystem.domain.entities.Task;
import io.chekarev.taskManagementSystem.domain.mappers.TaskMapper;
import io.chekarev.taskManagementSystem.exceptions.ResourceNotFoundException;
import io.chekarev.taskManagementSystem.repositories.TaskRepository;
import io.chekarev.taskManagementSystem.specifications.TaskSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("Test Task DTO");
    }

    @Test
    void testCreateTask() {
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.createTask(taskDTO);

        assertNotNull(result);
        assertEquals(taskDTO.getTitle(), result.getTitle());
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(taskDTO.getTitle(), result.getTitle());
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.updateTask(1L, taskDTO);

        assertNotNull(result);
        assertEquals(taskDTO.getTitle(), result.getTitle());
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));

        taskService.deleteTask(1L);

        Mockito.verify(taskRepository, Mockito.times(1)).delete(task);
    }
}
