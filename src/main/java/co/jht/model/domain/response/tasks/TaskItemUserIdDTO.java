package co.jht.model.domain.response.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskItemUserIdDTO {
    @JsonProperty("user_id")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}