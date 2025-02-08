package co.jht.service.impl;

import co.jht.exception.TaskNotFoundException;
import co.jht.generator.TaskCodeGenerator;
import co.jht.model.domain.persist.appuser.AppUser;
import co.jht.model.domain.persist.tasks.TaskItem;
import co.jht.repository.TaskRepository;
import co.jht.repository.UserRepository;
import co.jht.service.TaskService;
import co.jht.util.AuthUserUtil;
import co.jht.util.DateTimeFormatterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskCodeGenerator taskCodeGenerator;

    @Autowired
    public TaskServiceImpl(
            TaskRepository taskRepository,
            UserRepository userRepository,
            TaskCodeGenerator taskCodeGenerator
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskCodeGenerator = taskCodeGenerator;
    }

    @Override
    public List<TaskItem> getTasksByUserId(Long id) {
        logger.info("Fetching tasks for userId: {}", id);
        List<TaskItem> tasks = taskRepository.findByUserIdOrderById(id)
                .stream()
                .peek(task -> {
                    task.setCreationDate(task.getCreationDate().withZoneSameInstant(ZoneId.of(ASIA_TOKYO)));

                    if (task.getDueDate() != null) {
                        task.setDueDate(task.getDueDate().withZoneSameInstant(ZoneId.of(ASIA_TOKYO)));
                    }
                })
                .sorted(Comparator.comparing(TaskItem::getId))
                .collect(Collectors.toList());
        logger.info("Retrieved tasks: {}", tasks);
        return tasks;
    }

    @Override
    public TaskItem createTask(TaskItem createTask) {
        AppUser user = getLoginUser();
        TaskItem task = new TaskItem();
        task.setTaskCode(taskCodeGenerator.generateTaskCode());
        task.setTitle(createTask.getTitle());
        task.setDescription(createTask.getDescription());
        task.setDueDate(createTask.getDueDate());
        task.setCreationDate(DateTimeFormatterUtil.getCurrentTokyoTime());
        task.setUser(user);
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

    private AppUser getLoginUser() {
        return userRepository.findByUsername(AuthUserUtil.getAuthUsername());
    }

}