package co.jht.controller;

import co.jht.model.domain.persist.appuser.AppUser;
import co.jht.model.domain.response.appuser.AppUserDTO;
import co.jht.model.domain.response.mapper.AppUserMapper;
import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        List<AppUserDTO> userDTOs = users.stream()
                .map(appUserMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable("id") Long userId) {
        AppUser user = userService.getUserById(userId);
        return ResponseEntity.ok(appUserMapper.toDTO(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AppUserDTO> createUser(
            @RequestBody AppUserDTO userDTO
    ) {
        AppUser user = appUserMapper.toEntity(userDTO);
        AppUser createdUser = userService.createUser(user);
        return ResponseEntity.ok(appUserMapper.toDTO(createdUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email) {
        userService.registerUser(username, password, email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(
            @RequestParam String username,
            @RequestParam String password
    ) {
        String token = userService.authenticateUser(username, password);

        if (token != null) {
            // load user to current session context
            userService.loadUserByUsername(username);
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }
        return ResponseEntity.status(401).body("Invalid username or password!");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/profile/{username}")
    public ResponseEntity<AppUserDTO> getUserProfile(@PathVariable("username") String username) {
        AppUser user = userService.findByUsername(username);
        return ResponseEntity.ok(appUserMapper.toDTO(user));
    }
}