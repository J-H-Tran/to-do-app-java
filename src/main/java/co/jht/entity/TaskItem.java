package co.jht.entity;

import co.jht.serializer.ZonedDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;
import static co.jht.util.DateTimeFormatterUtil.getFormatter;

@Entity
@Table(name = "task_item_table")
public class TaskItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("title")
    @Column(nullable = false)
    private String title;

    @JsonProperty("description")
    @Column
    private String description;

    @JsonProperty("creation_date")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime creationDate;

    @JsonProperty("due_date")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime dueDate;

    @JsonProperty("complete")
    @Column(nullable = false)
    private boolean isComplete = false;

    @JsonProperty("user")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @PrePersist
    public void prePersist() {
        this.creationDate =
                ZonedDateTime.parse(
                    Objects.requireNonNullElseGet(
                        this.creationDate,
                        () -> ZonedDateTime.now(ZoneId.of(ASIA_TOKYO))
                    ).format(getFormatter())
                );

        if (this.dueDate != null) {
            this.dueDate = this.dueDate.withZoneSameInstant(ZoneId.of(ASIA_TOKYO))
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}