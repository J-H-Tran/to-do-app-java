package co.jht.service.impl;

import co.jht.exception.TaskNotFoundException;
import co.jht.model.domain.persist.tasks.TaskItem;
import co.jht.repository.TaskRepository;
import co.jht.service.TaskService;
import co.jht.util.DateTimeFormatterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskItem> getTasksByUserId(Long id) {
        logger.info("Fetching tasks for userId: {}", id);
        return taskRepository.findByUserIdOrderById(id).stream()
                .peek(task -> {
                    task.setCreationDate(task.getCreationDate().withZoneSameInstant(ZoneId.of(ASIA_TOKYO)));

                    if (task.getDueDate() != null) {
                        task.setDueDate(task.getDueDate().withZoneSameInstant(ZoneId.of(ASIA_TOKYO)));
                    }
                })
                .toList();
    }

    @Override
    public TaskItem createTask(TaskItem task) {
        task.setCreationDate(DateTimeFormatterUtil.getCurrentTokyoTime());
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
    public TaskItem updateTaskDueDate(Long taskId, ZonedDateTime dueDate) {
        Optional<TaskItem> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            TaskItem task = taskOptional.get();
            task.setDueDate(dueDate);

            return taskRepository.save(task);
        } else {
            logger.error("Task to update Due Date not found with id: {}", taskId);
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
    }

    @Override
    public TaskItem updateTaskCompleteStatus(Long taskId, boolean completeStatus) {
        Optional<TaskItem> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            TaskItem task = taskOptional.get();
            task.setCompleteStatus(completeStatus);

            return taskRepository.save(task);
        } else {
            logger.error("Task to update Complete Status not found with id: {}", taskId);
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }
    }
}