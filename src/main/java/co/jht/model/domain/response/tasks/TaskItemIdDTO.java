package co.jht.model.domain.response.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskItemIdDTO {
    @JsonProperty("task_id")
    private Long taskId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}