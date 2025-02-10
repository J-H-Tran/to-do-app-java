package co.jht.model.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NormalLogin {
    private String token;
    private long expiresIn;

    public NormalLogin(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}