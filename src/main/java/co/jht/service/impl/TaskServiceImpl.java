package co.jht.service.impl;

import co.jht.entity.TaskItem;
import co.jht.repository.TaskRepository;
import co.jht.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskItem createTask(TaskItem task) {
        return taskRepository.save(task);
    }

    @Override
    public TaskItem updateTask(TaskItem task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<TaskItem> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

}