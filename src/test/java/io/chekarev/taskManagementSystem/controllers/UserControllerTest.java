package io.chekarev.taskManagementSystem.controllers;

import io.chekarev.taskManagementSystem.domain.entities.User;
import io.chekarev.taskManagementSystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Успешное получение всех пользователей")
    void shouldGetAllUsersSuccess() throws Exception {
        mockMvc.perform(get("/users/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Ошибка получения всех пользователей (пустой список)")
    void shouldGetAllUsersEmpty() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Успешное обновление данных пользователя")
    void shouldUpdateUserSuccess() throws Exception {
        User user = new User();
        user.setId(1L);

        mockMvc.perform(put("/users/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated User\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void shouldDeleteUserSuccess() throws Exception {
        mockMvc.perform(delete("/users/admin/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Успешное получение текущего пользователя")
    void shouldGetCurrentUserSuccess() throws Exception {
        User user = new User();
        user.setName("currentUser");

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Успешное обновление данных текущего пользователя")
    void shouldUpdateCurrentUserSuccess() throws Exception {
        User user = new User();
        user.setName("updatedUser");

        mockMvc.perform(put("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedUser\"}"))
                .andExpect(status().isOk());
    }
}
