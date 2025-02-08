package co.jht.model.domain.response.mapper;

import co.jht.model.domain.persist.tasks.TaskItem;
import co.jht.model.domain.response.tasks.TaskItemCreateDTO;
import co.jht.model.domain.response.tasks.TaskItemCreatedDTO;
import co.jht.model.domain.response.tasks.TaskItemDTO;
import co.jht.model.domain.response.tasks.TaskItemIdDTO;
import co.jht.model.domain.response.tasks.TaskItemListedDTO;
import co.jht.model.domain.response.tasks.TaskItemUpdateDTO;
import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        dto.setTaskCode(task.getTaskCode());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCreationDate(task.getCreationDate());
        dto.setDueDate(task.getDueDate());
        dto.setCompleteStatus(task.getCompleteStatus());
        dto.setUserId(task.getUser().getId());
        return dto;
    }

    public List<TaskItemListedDTO> toDTO(List<TaskItem> tasks) {
        // implement
        return tasks.stream()
                .map(this::toListedDTO)
                .collect(Collectors.toList());
    }

    public TaskItemCreatedDTO toCreatedDTO(TaskItem task) {
        TaskItemCreatedDTO dto = new TaskItemCreatedDTO();
        dto.setTaskCode(task.getTaskCode());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCreationDate(task.getCreationDate());
        dto.setDueDate(task.getDueDate());
        dto.setCompleteStatus(task.getCompleteStatus());
        dto.setUserEmail(task.getUser().getEmail());
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
        return task;
    }

    public TaskItem toEntity(TaskItemUpdateDTO dto) {
        TaskItem task = new TaskItem();
        task.setTaskCode(dto.getTaskCode());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setCompleteStatus(dto.getCompleteStatus());
        return task;
    }

    public TaskItem toEntity(TaskItemIdDTO dto) {
        TaskItem task = new TaskItem();
        task.setId(dto.getTaskId());
        return task;
    }

    private TaskItemListedDTO toListedDTO(TaskItem task) {
        TaskItemListedDTO dto = new TaskItemListedDTO();
        dto.setTaskCode(task.getTaskCode());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCreationDate(task.getCreationDate());
        dto.setDueDate(task.getDueDate());
        dto.setCompleteStatus(task.getCompleteStatus());
        dto.setUserEmail(task.getUser().getEmail());
        return dto;
    }
}