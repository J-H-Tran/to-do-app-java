package co.jht.service.impl;

import co.jht.entity.tasks.TaskItem;
import co.jht.exception.TaskNotFoundException;
import co.jht.repository.TaskRepository;
import co.jht.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskItem createTask(TaskItem task) {
        task.setCreationDate(ZonedDateTime.now(ZoneId.of(ASIA_TOKYO)));
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
        return taskRepository.findByUserIdOrderById(userId).stream()
                .peek(task -> {
                    task.setCreationDate(task.getCreationDate().withZoneSameInstant(ZoneId.of(ASIA_TOKYO)));

                    if (task.getDueDate() != null) {
                      task.setDueDate(task.getDueDate().withZoneSameInstant(ZoneId.of(ASIA_TOKYO)));
                    }
                })
                .toList();
    }

    @Override
    public TaskItem updateTaskDueDate(Long taskId, ZonedDateTime dueDate) {
        Optional<TaskItem> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            TaskItem task = taskOptional.get();
            task.setDueDate(dueDate);

            return taskRepository.save(task);
        } else {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
    }
}