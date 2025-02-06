package co.jht.controller;

import co.jht.model.domain.persist.entity.tasks.TaskItem;
import co.jht.model.domain.response.dto.mapper.TaskItemMapper;
import co.jht.model.domain.response.dto.tasks.TaskItemDTO;
import co.jht.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskItemMapper taskItemMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskItemMapper taskItemMapper) {
        this.taskService = taskService;
        this.taskItemMapper = taskItemMapper;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskItemDTO>> getTasksByUserId(@PathVariable Long userId) {
        List<TaskItem> tasks = taskService.getTasksByUserId(userId);
        List<TaskItemDTO> taskDTOs = tasks.stream()
                .map(taskItemMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskDTOs);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<TaskItemDTO> createTask(@RequestBody TaskItemDTO taskDTO) {
        TaskItem task = taskItemMapper.toEntity(taskDTO);
        TaskItem createdTask = taskService.createTask(task);
        return ResponseEntity.ok(taskItemMapper.toDTO(createdTask));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskItemDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskItemDTO taskDTO) {
        TaskItem task = taskItemMapper.toEntity(taskDTO);
        task.setId(taskId);
        TaskItem updatedTask = taskService.updateTask(task);
        return ResponseEntity.ok(taskItemMapper.toDTO(updatedTask));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@RequestHeader("X-Task-Id") Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{taskId}/due_date")
    public ResponseEntity<TaskItemDTO> updateTaskDueDate(@PathVariable Long taskId, @RequestBody TaskItemDTO taskDTO) {
        TaskItem updateTask = taskService.updateTaskDueDate(taskId, taskDTO.getDueDate());
        return ResponseEntity.ok(taskItemMapper.toDTO(updateTask));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{taskId}/complete_status")
    public ResponseEntity<TaskItemDTO> updateCompleteStatus(@PathVariable Long taskId, @RequestBody TaskItemDTO taskDTO) {
        TaskItem updateTask = taskService.updateTaskCompleteStatus(taskId, taskDTO.getCompleteStatus());
        return ResponseEntity.ok(taskItemMapper.toDTO(updateTask));
    }
}