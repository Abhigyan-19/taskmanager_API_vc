//package com.example.taskManager.services;
//
//import com.example.taskManager.model.Task;
//import com.example.taskManager.repository.TaskRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TaskServiceTest {
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @InjectMocks
//    private TaskService taskService;
//
//    @Test
//    void shouldSaveNewTask() {
//        Task taskToSave = new Task();
//        taskToSave.setTitle("Test Task");
//
//        when(taskRepository.save(any(Task.class))).thenReturn(taskToSave);
//
//        Task savedTask = taskService.createTask(taskToSave);
//
//        assertNotNull(savedTask);
//        assertEquals("Test Task", savedTask.getTitle());
//        verify(taskRepository, times(1)).save(taskToSave);
//}
//}


package com.example.taskManager.services;

import com.example.taskManager.model.Task;
import com.example.taskManager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldSaveNewTask() {
        Task taskToSave = new Task();
        taskToSave.setTitle("Test Task");

        when(taskRepository.save(any(Task.class))).thenReturn(taskToSave);

        Task savedTask = taskService.createTask(taskToSave);

        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getTitle());
        verify(taskRepository, times(1)).save(taskToSave);
    }

    @Test
    void shouldReturnAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository).findAll();
    }

    @Test
    void shouldReturnTaskById() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertEquals(1L, foundTask.getId());
        verify(taskRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.getTaskById(99L));
        assertEquals("Task not found with id 99", exception.getMessage());
        verify(taskRepository).findById(99L);
    }

    @Test
    void shouldUpdateTask() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Desc");
        existingTask.setCompleted(false);

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("New Desc");
        updatedTask.setCompleted(true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Desc", result.getDescription());
        assertTrue(result.isCompleted());
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(task);
    }
}