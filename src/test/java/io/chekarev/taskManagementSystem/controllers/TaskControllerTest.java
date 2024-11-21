package io.chekarev.taskManagementSystem.controllers;

import io.chekarev.taskManagementSystem.domain.dto.TaskDTO;
import io.chekarev.taskManagementSystem.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        HandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService))
                .setCustomArgumentResolvers(pageableResolver)
                .build();
    }

    @Test
    @DisplayName("Получение всех задач с фильтрацией и пагинацией для администратора - задачи найдены")
    void shouldGetAllTasks() throws Exception {
        Page<TaskDTO> tasks = new PageImpl<>(Collections.singletonList(new TaskDTO()), PageRequest.of(0, 10), 1);
        when(taskService.getAllTasks(any(Pageable.class), any(), any(), any(), any()))
                .thenReturn(tasks);

        mockMvc.perform(get("/tasks/admin")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Получение всех задач с фильтрацией и пагинацией для администратора - задачи не найдены")
    void shouldGetAllTasksEmpty() throws Exception {
        Page<TaskDTO> tasks = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(taskService.getAllTasks(any(Pageable.class), any(), any(), any(), any()))
                .thenReturn(tasks);

        mockMvc.perform(get("/tasks/admin")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Создание новой задачи (админ) - успешное создание")
    void shouldCreateTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(taskDTO);

        mockMvc.perform(post("/tasks/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Удаление задачи по ID (админ) - успешное удаление")
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/admin/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Получение задачи по ID (админ) - задача найдена")
    void shouldGetTaskById() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.getTaskById(1L)).thenReturn(taskDTO);

        mockMvc.perform(get("/tasks/admin/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
