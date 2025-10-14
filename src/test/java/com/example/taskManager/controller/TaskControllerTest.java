package com.example.taskManager.controller;

import com.example.taskManager.Exception.GlobalExceptionHandler;
import com.example.taskManager.model.Task;
import com.example.taskManager.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper;

    @BeforeEach
    
    void setup() {
    mockMvc = MockMvcBuilders
            .standaloneSetup(taskController)
            .setControllerAdvice(new GlobalExceptionHandler()) // ✅ Include exception handler
            .build();
    objectMapper = new ObjectMapper();
}
    //void setup() {
    //    mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    //    objectMapper = new ObjectMapper();
    //}

    @Test
    void shouldCreateTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Create API Test");
        task.setDescription("Testing POST endpoint");

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Create API Test"));
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Get All API Test");

        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Get All API Test"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Get By ID Test");

//        when(taskService.getTaskById(1L)).thenReturn((Optional.of(task));
        when(taskService.getTaskById(1L)).thenReturn(task);


        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Get By ID Test"));
}
    @Test
void shouldDeleteTask() throws Exception {
    doNothing().when(taskService).deleteTask(1L);

    mockMvc.perform(delete("/api/tasks/1"))
            .andExpect(status().isNoContent());
}
    @Test
void shouldUpdateTask() throws Exception {
    Task updatedTask = new Task();
    updatedTask.setId(1L);
    updatedTask.setTitle("Updated Title");
    updatedTask.setDescription("Updated Description");
    updatedTask.setCompleted(true);

    when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

    mockMvc.perform(put("/api/tasks/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedTask)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Updated Title"));
}
    @Test
void shouldHandleRuntimeException() throws Exception {
    when(taskService.getTaskById(999L)).thenThrow(new RuntimeException("Task not found with id 999"));

    mockMvc.perform(get("/api/tasks/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Task not found with id 999"))
            .andExpect(jsonPath("$.timestamp").exists()); // ✅ Verifies timestamp is present
}
}
