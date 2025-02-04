package co.jht.controller;

import co.jht.entity.TaskItem;
import co.jht.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskItem>> getTasksByUserId(
            @RequestHeader("X-User-Id") Long userId
    ) {
        List<TaskItem> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskItem> createTask(
            @RequestBody TaskItem task
    ) {
        TaskItem createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping
    public ResponseEntity<TaskItem> updateTask(
            @RequestHeader("X-Task-Id") Long taskId,
            @RequestBody TaskItem task
    ) {
        task.setId(taskId);
        TaskItem updatedTask = taskService.updateTask(task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTask(
            @RequestHeader("X-Task-Id") Long taskId
    ) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}