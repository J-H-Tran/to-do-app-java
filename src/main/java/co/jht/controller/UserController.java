package co.jht.controller;

import co.jht.entity.AppUser;
import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<AppUser> getUser(
            @RequestHeader("X-Username") String username
    ) {
        AppUser user = userService.findByUsername(username);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody AppUser user
    ) {
        userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());
        return ResponseEntity.ok("Registration successful.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody AppUser user
    ) {
        String token = userService.authenticateUser(user.getUsername(), user.getPassword());

        if (token != null) {
            userService.loadUserByUsername(user.getUsername());

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .build();
        } else {
            return ResponseEntity.status(401).body("Invalid username or password!");
        }
    }
}