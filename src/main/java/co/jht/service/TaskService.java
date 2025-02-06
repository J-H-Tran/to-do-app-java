package co.jht.service;

import co.jht.model.domain.persist.entity.tasks.TaskItem;

import java.time.ZonedDateTime;
import java.util.List;

public interface TaskService {
    List<TaskItem> getTasksByUserId(Long userId);
    TaskItem createTask(TaskItem task);
    TaskItem updateTask(TaskItem task);
    void deleteTask(Long taskId);
    TaskItem updateTaskDueDate(Long taskId, ZonedDateTime dueDate);
    TaskItem updateTaskCompleteStatus(Long taskId, boolean completeStatus);
}