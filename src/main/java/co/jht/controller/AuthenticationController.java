//package co.jht.controller;
//
//import co.jht.model.domain.entity.appuser.AppUser;
//import co.jht.model.domain.response.NormalLogin;
//import co.jht.model.domain.response.appuser.LoginUserDto;
//import co.jht.model.domain.response.appuser.RegisterUserDto;
//import co.jht.model.domain.response.appuser.VerifyUserDto;
//import co.jht.service.impl.AuthenticationService;
//import co.jht.service.impl.JwtService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthenticationController {
//
//    private final JwtService jwtService;
//    private final AuthenticationService authenticationService;
//
//
//    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
//        this.jwtService = jwtService;
//        this.authenticationService = authenticationService;
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<AppUser> register(
//            @RequestBody RegisterUserDto dto
//            ) {
//        AppUser registerUser = authenticationService.signup(dto);
//        return ResponseEntity.ok(registerUser);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<NormalLogin> authenticate(
//            @RequestBody LoginUserDto dto
//            ) {
//        AppUser authenticatedUser = authenticationService.authenticate(dto);
//        String jwtToken = jwtService.generateToken(authenticatedUser);
//        NormalLogin normalLoginResponse = new NormalLogin(jwtToken, jwtService.getExpirationTime());
//        return ResponseEntity.ok()
//                .header("Authorization", "Bearer " + jwtToken)
//                .body(normalLoginResponse);
//    }
//
//    @PostMapping("/verify")
//    public ResponseEntity<?> verifyUser(
//            @RequestBody VerifyUserDto dto
//    ) {
//        try {
//            authenticationService.verifyUser(dto);
//            return ResponseEntity.ok("Account verified successfully");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/resend")
//    public ResponseEntity<?> resendVerificationCode(
//            @RequestParam String email
//    ) {
//        try {
//            authenticationService.resendVerificationCode(email);
//            return ResponseEntity.ok("Verification code sent");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout() {
//        authenticationService.revokeAuthentication();
//        return ResponseEntity.ok("Logout successful");
//    }
//}