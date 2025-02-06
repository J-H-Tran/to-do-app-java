package co.jht.model.domain.response.dto.mapper;

import co.jht.model.domain.persist.entity.appuser.AppUser;
import co.jht.model.domain.response.dto.appuser.AppUserDTO;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUserDTO toDTO(AppUser user) {
        if (user == null) {
            return null;
        }

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setAppUserId(user.getId());
        appUserDTO.setUserName(user.getUserName());
        appUserDTO.setEmail(user.getEmail());
        appUserDTO.setFirstName(user.getFirstName());
        appUserDTO.setLastName(user.getLastName());
        appUserDTO.setProfilePictureUrl(user.getProfilePictureUrl());
        appUserDTO.setRegistrationDate(user.getRegistrationDate());
        appUserDTO.setAccountStatus(user.getAccountStatus());
        appUserDTO.setRole(user.getRole());

        return appUserDTO;
    }

    public AppUser toEntity(AppUserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        AppUser appUser = new AppUser();
        appUser.setId(userDTO.getAppUserId());
        appUser.setUserName(userDTO.getUserName());
        appUser.setEmail(userDTO.getEmail());
        appUser.setFirstName(userDTO.getFirstName());
        appUser.setLastName(userDTO.getLastName());
        appUser.setProfilePictureUrl(userDTO.getProfilePictureUrl());
        appUser.setRegistrationDate(userDTO.getRegistrationDate());
        appUser.setAccountStatus(userDTO.getAccountStatus());
        appUser.setRole(userDTO.getRole());

        return appUser;
    }
}