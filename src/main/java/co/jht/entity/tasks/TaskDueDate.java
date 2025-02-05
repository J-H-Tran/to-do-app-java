package co.jht.entity.tasks;

import co.jht.serializer.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.ZonedDateTime;

public class TaskDueDate {

    @JsonProperty("due_date")
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime dueDate;

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }
}