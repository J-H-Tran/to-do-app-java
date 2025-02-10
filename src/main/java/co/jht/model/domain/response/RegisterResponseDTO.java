package co.jht.model.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterResponseDTO {
    @JsonProperty("message")
    private String message;

    public RegisterResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}