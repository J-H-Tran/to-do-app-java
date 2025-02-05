package co.jht.service;

import co.jht.entity.TaskItem;

import java.util.List;

public interface TaskService {
    TaskItem createTask(TaskItem task);
    TaskItem updateTask(TaskItem task);
    void deleteTask(Long taskId);
    List<TaskItem> getTasksByUserId(Long userId);
}