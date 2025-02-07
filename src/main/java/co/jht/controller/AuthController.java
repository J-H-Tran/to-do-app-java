package co.jht.controller;

import co.jht.model.domain.persist.appuser.AppUser;
import co.jht.model.domain.response.appuser.AppUserLoginDTO;
import co.jht.model.domain.response.appuser.AppUserRegisterDTO;
import co.jht.model.domain.response.mapper.AppUserMapper;
import co.jht.service.UserService;
import co.jht.util.AuthUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

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
        logger.info("User registered successfully: {}, Welcome!", newUser.getUsername());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody AppUserLoginDTO userLoginDTO) {
        AppUser user = appUserMapper.toEntity(userLoginDTO);
        String token = userService.authenticateUser(user);

        if (token != null) {
            UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            UsernamePasswordAuthenticationToken auth = AuthUserUtil.setAuthUserContext(userDetails);

            logger.info("User logged in successfully: {}", user.getUsername());
            logger.info("User's listed authority: {}", auth.getAuthorities().toString());
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .build();
        }
        logger.error("Invalid username or password, please try again.");
        return ResponseEntity.status(401).body("Invalid username or password!");
    }
}