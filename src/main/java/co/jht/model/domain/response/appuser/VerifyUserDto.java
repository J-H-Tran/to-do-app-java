package co.jht.model.domain.response.appuser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDto {
    private String email;
    private String verificationCode;

    @Override
    public String toString() {
        return "VerifyUserDto{" +
                "email='" + email + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}