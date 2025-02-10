package co.jht.model.domain.response.appuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IfteLoginDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}