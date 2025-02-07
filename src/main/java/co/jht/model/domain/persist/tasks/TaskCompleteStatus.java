package co.jht.model.domain.persist.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskCompleteStatus {

    @JsonProperty("complete_status")
    private boolean completeStatus;

    public boolean getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(boolean completeStatus) {
        this.completeStatus = completeStatus;
    }
}