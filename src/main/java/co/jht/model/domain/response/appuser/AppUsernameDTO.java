package co.jht.model.domain.response.appuser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppUsernameDTO implements AppUserDTOBase {
    @JsonProperty("username")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}