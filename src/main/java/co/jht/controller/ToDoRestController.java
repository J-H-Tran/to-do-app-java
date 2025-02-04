package co.jht.controller;

import co.jht.entity.AppUser;
import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ToDoRestController {

    private UserService userService;

    @Autowired
    public ToDoRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<AppUser> getUser(@PathVariable String username) {
        AppUser user = userService.findByUsername(username);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AppUser user) {
        userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());
        return ResponseEntity.ok("Registration successful.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AppUser user) {
        if (userService.authenticateUser(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok("Login successful.");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password!");
        }
    }


}