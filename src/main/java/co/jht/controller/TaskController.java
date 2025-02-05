package co.jht.controller;

import co.jht.entity.tasks.TaskDueDate;
import co.jht.entity.tasks.TaskItem;
import co.jht.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<TaskItem>> getTasksByUserId(
            @RequestHeader("X-User-Id") Long userId
    ) {
        List<TaskItem> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<TaskItem> createTask(
            @RequestBody TaskItem task
    ) {
        TaskItem createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping
    public ResponseEntity<TaskItem> updateTask(
            @RequestHeader("X-Task-Id") Long taskId,
            @RequestBody TaskItem task
    ) {
        task.setId(taskId);
        TaskItem updatedTask = taskService.updateTask(task);
        return ResponseEntity.ok(updatedTask);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public ResponseEntity<Void> deleteTask(
            @RequestHeader("X-Task-Id") Long taskId
    ) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/due_date")
    public ResponseEntity<TaskItem> updateTaskDueDate(
            @RequestHeader("X-Task-Id") Long taskId,
            @RequestBody TaskDueDate taskDueDate
    ) {
        TaskItem updateTask = taskService.updateTaskDueDate(taskId, taskDueDate.getDueDate());
        return ResponseEntity.ok(updateTask);
    }
}