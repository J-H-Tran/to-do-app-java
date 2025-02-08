package co.jht.controller;

import co.jht.model.domain.persist.appuser.AppUser;
import co.jht.model.domain.response.appuser.AppUserLoginDTO;
import co.jht.model.domain.response.appuser.AppUserRegisterDTO;
import co.jht.model.domain.response.mapper.AppUserMapper;
import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AppUserMapper appUserMapper;

    @Autowired
    public AuthController(UserService userService, AppUserMapper appUserMapper) {
        this.userService = userService;
        this.appUserMapper = appUserMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody AppUserRegisterDTO userRegisterDTO) {
        AppUser newUser = appUserMapper.toEntity(userRegisterDTO);
        userService.registerUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody AppUserLoginDTO userLoginDTO) {
        AppUser user = appUserMapper.toEntity(userLoginDTO);
        String token = userService.authenticateUser(user);

        if (token != null) {
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
    }
}