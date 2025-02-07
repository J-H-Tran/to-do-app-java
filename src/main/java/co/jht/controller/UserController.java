package co.jht.controller;

import co.jht.model.domain.persist.appuser.AppUser;
import co.jht.model.domain.response.appuser.AppUserDTO;
import co.jht.model.domain.response.appuser.AppUserIdDTO;
import co.jht.model.domain.response.appuser.AppUserUpdateDTO;
import co.jht.model.domain.response.appuser.AppUsernameDTO;
import co.jht.model.domain.response.mapper.AppUserMapper;
import co.jht.service.UserService;
import co.jht.util.AuthUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AppUserMapper appUserMapper;

    @Autowired
    public UserController(UserService userService, AppUserMapper appUserMapper) {
        this.userService = userService;
        this.appUserMapper = appUserMapper;
    }

    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        List<AppUserDTO> userDTOs = appUserMapper.toDTOList(users);
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/user")
    public ResponseEntity<AppUserDTO> getUserById(@RequestBody AppUserIdDTO appUserIdDTO) {
        AppUser user = userService.getUserById(appUserIdDTO.getId());
        return ResponseEntity.ok(appUserMapper.toDTO(user));
    }

    @PostMapping("/create")
    public ResponseEntity<AppUserDTO> createUser(@RequestBody AppUserDTO userDTO) {
        AppUser user = appUserMapper.toEntity(userDTO);
        AppUser createdUser = userService.createUser(user);
        return ResponseEntity.ok(appUserMapper.toDTO(createdUser));
    }

    @PutMapping("/update")
    public ResponseEntity<AppUserDTO> updateUser(@RequestBody AppUserUpdateDTO userDTO) {
        String authUsername = AuthUserUtil.getAuthUsername();
        AppUser editUser = appUserMapper.toEntity(userDTO);
        AppUser updatedUser = userService.updateUser(editUser, authUsername);
        return ResponseEntity.ok(appUserMapper.toDTO(updatedUser));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody AppUserIdDTO appUserIdDTO) {
        userService.deleteUser(appUserIdDTO.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<AppUserDTO> getUserProfile(@RequestBody AppUsernameDTO appUsernameDTO) {
        AppUser user = userService.findByUsername(appUsernameDTO.getUsername());
        return ResponseEntity.ok(appUserMapper.toDTO(user));
    }
}