package co.jht.model.domain.response.appuser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppUserIdDTO {
    @JsonProperty("user_id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}