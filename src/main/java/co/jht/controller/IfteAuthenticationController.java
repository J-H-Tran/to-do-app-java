package co.jht.controller;

import co.jht.model.domain.entity.appuser.IfteUser;
import co.jht.model.domain.response.IfteAuthenticationResponse;
import co.jht.model.domain.response.appuser.IfteLoginDto;
import co.jht.model.domain.response.appuser.IfteRegisterDto;
import co.jht.model.domain.response.mapper.UserMapper;
import co.jht.service.impl.IfteAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class IfteAuthenticationController {

    private final IfteAuthenticationService authenticationService;
    private final UserMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(IfteAuthenticationController.class);

    public IfteAuthenticationController(IfteAuthenticationService authenticationService, UserMapper userMapper) {
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<IfteAuthenticationResponse> register(@RequestBody IfteRegisterDto dto) {
        IfteUser request = userMapper.toEntity(dto);
        return ResponseEntity.ok(authenticationService.register((request)));
    }

    @PostMapping("/login")
    public ResponseEntity<IfteAuthenticationResponse> login(@RequestBody IfteLoginDto dto) {
        IfteUser request = userMapper.toEntity(dto);
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<IfteAuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
logger.info("=> Received request for refresh");
        IfteAuthenticationResponse refreshResponse = authenticationService.refreshToken(request, response);
        if (response == null) {
logger.info("=> Refresh failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
logger.info("=> Refresh finished");
        return ResponseEntity.ok(refreshResponse);
    }
}