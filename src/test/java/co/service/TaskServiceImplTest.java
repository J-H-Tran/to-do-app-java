package co.service;

import co.jht.exception.TaskNotFoundException;
import co.jht.model.domain.persist.tasks.TaskItem;
import co.jht.repository.TaskRepository;
import co.jht.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private ZonedDateTime timestamp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        timestamp = ZonedDateTime.parse("2025-01-01T09:00:00+09:00");
    }

    private TaskItem createTaskItem() {
        TaskItem task = new TaskItem();
        task.setCreationDate(timestamp);
        return task;
    }

    @Test
    void testGetTasksByUserId() {
        TaskItem task1 = createTaskItem();
        TaskItem task2 = createTaskItem();

        when(taskRepository.findByUserIdOrderById(1L)).thenReturn(Arrays.asList(task1, task2));

        List<TaskItem> tasks = taskService.getTasksByUserId(1L);

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findByUserIdOrderById(1L);
    }


    @Test
    void testCreateTask() {
        TaskItem task = new TaskItem();
        when(taskRepository.save(task)).thenReturn(task);

        TaskItem createdTask = taskService.createTask(task);

        assertNotNull(createdTask);
        assertEquals(ZonedDateTime.now(ZoneId.of(ASIA_TOKYO)).getZone(), createdTask.getCreationDate().getZone());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask() {
        TaskItem task = new TaskItem();
        when(taskRepository.save(task)).thenReturn(task);

        TaskItem updatedTask = taskService.updateTask(task);

        assertNotNull(updatedTask);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateTaskDueDate() {
        TaskItem task = new TaskItem();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        ZonedDateTime dueDate = ZonedDateTime.now();
        TaskItem updatedTask = taskService.updateTaskDueDate(1L, dueDate);

        assertNotNull(updatedTask);
        assertEquals(dueDate, updatedTask.getDueDate());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTaskCompleteStatus() {
        TaskItem task = new TaskItem();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        TaskItem updatedTask = taskService.updateTaskCompleteStatus(1L, true);

        assertNotNull(updatedTask);
        assertTrue(updatedTask.getCompleteStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTaskDueDate_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskDueDate(1L, ZonedDateTime.now()));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTaskCompleteStatus_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskCompleteStatus(1L, true));
        verify(taskRepository, times(1)).findById(1L);
    }
}