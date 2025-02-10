//package co.jht.service.impl;
//
//import co.jht.exception.UserNotFoundException;
//import co.jht.model.domain.entity.appuser.AppUser;
//import co.jht.model.domain.response.appuser.LoginUserDto;
//import co.jht.model.domain.response.appuser.RegisterUserDto;
//import co.jht.model.domain.response.appuser.VerifyUserDto;
//import co.jht.repository.UserRepository;
//import jakarta.mail.MessagingException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.stereotype.Service;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.time.LocalDateTime;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.Random;
//
//@Service
//public class AuthenticationService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//    private final EmailService emailService;
//    private final JwtService jwtService;
//
//    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
//
//    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, JwtService jwtService) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
//        this.emailService = emailService;
//        this.jwtService = jwtService;
//    }
//
//    public AppUser signup(RegisterUserDto dto) {
//        AppUser user = new AppUser(dto.getEmail(), dto.getUsername(), passwordEncoder.encode(dto.getPassword()));
//        user.setVerificationCode(generateVerificationCode());
//        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
//        user.setEnabled(false);
//
//        sendVerificationEmail(user);
//        return userRepository.save(user);
//    }
//
//    public AppUser authenticate(LoginUserDto dto) {
//        logger.info("LoginUserDto email sent from RequestBody: {}", dto.getEmail());
//        logger.info("LoginUserDto pass sent from RequestBody: {}", dto.getPassword());
//
//        AppUser user = dto.getEmail().contains("@") ?
//                userRepository.findByEmail(dto.getEmail())
//                        .orElseThrow(() -> new UserNotFoundException("User not found")) :
//                userRepository.findByUsername(dto.getEmail())
//                        .orElseThrow(() -> new UserNotFoundException("User not found"));
//        logger.info("AppUser email retrieved from DB findByEmail: {}", user.getEmail());
//        logger.info("AppUser username retrieved from DB findByEmail: {}", user.getUsername());
//        logger.info("AppUser pass retrieved from DB findByEmail: {}", user.getPassword());
//
//        if (!user.isEnabled()) {
//            throw new RuntimeException("Account not verified. Please verify your account");
//        }
//
//        logger.info("Attempting to authenticate the user by AuthenticationManager");
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                    user.getUsername(), // User username as principal
//                    dto.getPassword() // Raw password handled by authentication manager provided with decoder
//                )
//        );
//        logger.info("Authentication was successful for user: {}", user);
//        return user;
//    }
//
//    public void verifyUser(VerifyUserDto dto) {
//        Optional<AppUser> optUser = userRepository.findByEmail(dto.getEmail());
//
//        if (optUser.isPresent()) {
//            AppUser user = optUser.get();
//
//            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
//                throw new RuntimeException("Verification code has expired");
//            }
//            if (user.getVerificationCode().equals(dto.getVerificationCode())) {
//                user.setEnabled(true);
//                user.setVerificationCode(null);
//                user.setVerificationCodeExpiresAt(null);
//                userRepository.save(user);
//            } else {
//                throw new RuntimeException("Invalid verification code");
//            }
//        } else {
//            throw new UserNotFoundException("User not found");
//        }
//    }
//
//    public void resendVerificationCode(String email) {
//        Optional<AppUser> optUser = userRepository.findByEmail(email);
//
//        if (optUser.isPresent()) {
//            AppUser user = optUser.get();
//
//            if (user.isEnabled()) {
//                throw new RuntimeException("Account is already verified");
//            }
//            user.setVerificationCode(generateVerificationCode());
//            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
//            userRepository.save(user);
//        } else {
//            throw new UserNotFoundException("User not found");
//        }
//    }
//
//    public void revokeAuthentication() {
//        SecurityContextHolder.clearContext();
//        logger.info("Current SecurityContext cleared");
//
//        // change the JWT expiration Date.now()
//        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
//                .getRequest().getHeader("Authorization").substring(7);
//        logger.info("JWT to invalidate: {}", token);
//        jwtService.invalidateToken(token);
//
//        // Clear cookies, if any
//        logger.info("Clearing cookies, if any");
//        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = attr.getRequest();
//        HttpServletResponse response = attr.getResponse();
//        if (response != null) {
//            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
//            logoutHandler.logout(request, response, null);
//        }
//    }
//
//    private void sendVerificationEmail(AppUser user) {
//        String subject = "Account Verification";
//        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
//        String htmlMessage = "<html>"
//                + "<body style=\"font-family: Arial, sans-serif;\">"
//                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
//                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
//                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
//                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
//                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
//                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
//                + "</div>"
//                + "</div>"
//                + "</body>"
//                + "</html>";
//
//        try {
//            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
//        } catch (MessagingException e) {
//            // Handle email sending exception
//            logger.error(e.getMessage());
//        }
//    }
//
//    private String generateVerificationCode() {
//        Random rand = new Random();
//        int code = rand.nextInt(900000) + 100000;
//        return String.valueOf(code);
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}