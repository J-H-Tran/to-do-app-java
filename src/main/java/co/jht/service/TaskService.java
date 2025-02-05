package co.jht.service;

import co.jht.entity.tasks.TaskItem;

import java.time.ZonedDateTime;
import java.util.List;

public interface TaskService {
    TaskItem createTask(TaskItem task);
    TaskItem updateTask(TaskItem task);
    void deleteTask(Long taskId);
    List<TaskItem> getTasksByUserId(Long userId);
    TaskItem updateTaskDueDate(Long taskId, ZonedDateTime dueDate);
    TaskItem updateTaskCompleteStatus(Long taskId, boolean completeStatus);
}