package io.chekarev.taskManagementSystem.controllers;

import io.chekarev.taskManagementSystem.domain.dto.CommentDTO;
import io.chekarev.taskManagementSystem.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CommentController(commentService))
                .build();
    }


    @Test
    @DisplayName("Получение комментариев по ID задачи для администратора")
    void shouldGetCommentsByTaskIdAdmin() throws Exception {
        Mockito.when(commentService.getCommentsByTaskId(1L))
                .thenReturn(Collections.singletonList(new CommentDTO()));

        mockMvc.perform(get("/comments/admin/task/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Удаление комментария администратором")
    void shouldDeleteCommentAdmin() throws Exception {
        mockMvc.perform(delete("/comments/admin/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Добавление нового комментария")
    void shouldAddComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        Mockito.when(commentService.addComment(any(CommentDTO.class))).thenReturn(commentDTO);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
