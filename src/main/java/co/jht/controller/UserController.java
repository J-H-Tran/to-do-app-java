package co.jht.controller;

import co.jht.model.domain.persist.appuser.AppUser;
import co.jht.model.domain.response.appuser.AppUserDTO;
import co.jht.model.domain.response.appuser.AppUserIdDTO;
import co.jht.model.domain.response.mapper.AppUserMapper;
import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<AppUserDTO>> getAllUsers() { // get DTO -> Entity -> process -> return DTO
        List<AppUser> users = userService.getAllUsers();
        List<AppUserDTO> userDTOs = users.stream()
                .map(appUserMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/user")
    public ResponseEntity<AppUserDTO> getUserById(@RequestBody AppUserIdDTO appUserIdDTO) {
        AppUser user = userService.getUserById(appUserIdDTO.getId());
        return ResponseEntity.ok(appUserMapper.toDTO(user));
    }

    @PostMapping
    public ResponseEntity<AppUserDTO> createUser(
            @RequestBody AppUserDTO userDTO
    ) {
        AppUser user = appUserMapper.toEntity(userDTO);
        AppUser createdUser = userService.createUser(user);
        return ResponseEntity.ok(appUserMapper.toDTO(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDTO> updateUser(
            @PathVariable("id") Long userId,
            @RequestBody AppUserDTO userDTO
    ) {
        AppUser user = appUserMapper.toEntity(userDTO);
        user.setId(userId);

        AppUser updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(appUserMapper.toDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<AppUserDTO> getUserProfile(@PathVariable("username") String username) {
        AppUser user = userService.findByUsername(username);
        return ResponseEntity.ok(appUserMapper.toDTO(user));
    }
}