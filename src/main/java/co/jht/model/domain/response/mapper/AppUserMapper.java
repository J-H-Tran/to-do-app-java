//package co.jht.model.domain.response.mapper;
//
//import co.jht.model.domain.persist.appuser.AppUser;
//import co.jht.model.domain.response.appuser.AppUserDTO;
//import co.jht.model.domain.response.appuser.AppUserLoginDTO;
//import co.jht.model.domain.response.appuser.AppUserRegisterDTO;
//import co.jht.model.domain.response.appuser.AppUserUpdateDTO;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AppUserMapper implements BaseMapper<AppUser, AppUserDTO> {
//
//    @Override
//    public AppUserDTO toDTO(AppUser user) {
//        if (user == null) {
//            return null;
//        }
//        AppUserDTO dto = new AppUserDTO();
//        dto.setUsername(user.getUsername());
//        dto.setPassword(user.getPassword());
//        dto.setEmail(user.getEmail());
//        dto.setFirstName(user.getFirstName());
//        dto.setLastName(user.getLastName());
//        dto.setProfilePictureUrl(user.getProfilePictureUrl());
//        dto.setRegistrationDate(user.getRegistrationDate());
//        dto.setAccountStatus(user.getAccountStatus());
//        dto.setRole(user.getRole());
//
//        return dto;
//    }
//
//    @Override
//    public AppUser toEntity(AppUserDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//        AppUser user = new AppUser();
//        user.setUsername(dto.getUsername());
//        user.setPassword(dto.getPassword());
//        user.setEmail(dto.getEmail());
//        user.setFirstName(dto.getFirstName());
//        user.setLastName(dto.getLastName());
//        user.setProfilePictureUrl(dto.getProfilePictureUrl());
//        user.setRegistrationDate(dto.getRegistrationDate());
//        user.setAccountStatus(dto.getAccountStatus());
//        user.setRole(dto.getRole());
//
//        return user;
//    }
//
//    public AppUser toEntity(AppUserRegisterDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//        AppUser user = new AppUser();
//        user.setUsername(dto.getUsername());
//        user.setPassword(dto.getPassword());
//        user.setEmail(dto.getEmail());
//        user.setFirstName(dto.getFirstName());
//        user.setLastName(dto.getLastName());
//
//        return user;
//    }
//
//    public AppUser toEntity(AppUserLoginDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//        AppUser user = new AppUser();
//        user.setUsername(dto.getUsername());
//        user.setPassword(dto.getPassword());
//
//        return user;
//    }
//
//    public AppUser toEntity(AppUserUpdateDTO dto) {
//        AppUser user = new AppUser();
//        user.setUsername(dto.getUsername());
//        user.setPassword(dto.getPassword());
//        user.setEmail(dto.getEmail());
//        user.setFirstName(dto.getFirstName());
//        user.setLastName(dto.getLastName());
//        user.setProfilePictureUrl(dto.getProfilePictureUrl());
//        user.setAccountStatus(dto.getAccountStatus());
//
//        return user;
//    }
//}