package co.jht.model.domain.response.mapper;

import co.jht.model.domain.entity.appuser.IfteUser;
import co.jht.model.domain.response.appuser.IfteLoginDto;
import co.jht.model.domain.response.appuser.IfteRegisterDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public IfteRegisterDto toDTO(IfteUser user) {
        if (user == null) {
            return null;
        }
        IfteRegisterDto dto = new IfteRegisterDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    public IfteUser toEntity(IfteRegisterDto dto) {
        if (dto == null) {
            return null;
        }
        IfteUser user = new IfteUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }

    public IfteUser toEntity(IfteLoginDto dto) {
        if (dto == null) {
            return null;
        }
        IfteUser user = new IfteUser();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}