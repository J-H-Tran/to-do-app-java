package co.jht.model.domain.response.appuser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppUserLoginDTO implements AppUserDTOBase {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}