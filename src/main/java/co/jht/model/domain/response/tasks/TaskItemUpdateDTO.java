package co.jht.model.domain.response.tasks;

import co.jht.serializer.ZonedDateTimeDeserializer;
import co.jht.serializer.ZonedDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class TaskItemUpdateDTO {
    @JsonProperty("task_code")
    private String taskCode;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("due_date")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime dueDate;

    @JsonProperty("complete_status")
    private boolean completeStatus;

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(boolean completeStatus) {
        this.completeStatus = completeStatus;
    }
}