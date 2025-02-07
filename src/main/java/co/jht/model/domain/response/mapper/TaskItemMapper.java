package co.jht.model.domain.response.mapper;

import co.jht.model.domain.persist.tasks.TaskItem;
import co.jht.model.domain.response.tasks.TaskItemCreateDTO;
import co.jht.model.domain.response.tasks.TaskItemDTO;
import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskItemMapper {

    private final UserService userService;

    @Autowired
    public TaskItemMapper(UserService userService) {
        this.userService = userService;
    }

    public TaskItemDTO toDTO(TaskItem task) {
        TaskItemDTO dto = new TaskItemDTO();
        dto.setTaskId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCreationDate(task.getCreationDate());
        dto.setDueDate(task.getDueDate());
        dto.setCompleteStatus(task.getCompleteStatus());
        dto.setUserId(task.getId());

        return dto;
    }

    public TaskItem toEntity(TaskItemDTO dto) {
        TaskItem task = new TaskItem();
        task.setId(dto.getTaskId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCreationDate(dto.getCreationDate());
        task.setDueDate(dto.getDueDate());
        task.setCompleteStatus(dto.getCompleteStatus());
        task.setUser(userService.getUserById(dto.getUserId()));

        return task;
    }

    public TaskItem toEntity(TaskItemCreateDTO dto) {
        TaskItem task = new TaskItem();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setCompleteStatus(dto.getCompleteStatus());

        return task;
    }
}