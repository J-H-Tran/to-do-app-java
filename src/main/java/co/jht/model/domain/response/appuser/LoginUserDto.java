package co.jht.model.domain.response.appuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
}