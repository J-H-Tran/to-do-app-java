package co.jht.service.impl;

import co.jht.exception.UserNotFoundException;
import co.jht.model.domain.entity.appuser.IfteUser;
import co.jht.model.domain.entity.token.IfteToken;
import co.jht.model.domain.response.IfteAuthenticationResponse;
import co.jht.repository.IfteTokenRepository;
import co.jht.repository.IfteUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IfteAuthenticationService {

    private final IfteUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IfteJwtService jwtService;
    private final IfteTokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    private final Logger logger = LoggerFactory.getLogger(IfteAuthenticationService.class);

    public IfteAuthenticationService(
            IfteUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            IfteJwtService jwtService,
            IfteTokenRepository tokenRepository,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public IfteAuthenticationResponse register(IfteUser userRequest) {
        boolean isUserInDatabase = userRepository.findByUsername(userRequest.getUsername()).isPresent();

        // Check if user already exists. If true, then do not authenticate the user
        if (isUserInDatabase) {
            return new IfteAuthenticationResponse(null, null, "User already exists");
        }

        // Try to authenticate the user
        IfteUser user = new IfteUser();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());

        user = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Persists the user's tokens to the DB
        saveUserToken(accessToken, refreshToken, user);

        return new IfteAuthenticationResponse(accessToken, refreshToken, "User registration successful");
    }

    public IfteAuthenticationResponse authenticate(IfteUser userRequest) {
        // Pass credentials to AuthenticationManager to authenticate the user attempting to log in
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()
                )
        );

        IfteUser user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return new IfteAuthenticationResponse(accessToken, refreshToken, "User login successful");
    }

    @Transactional
    public IfteAuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // Extract token from Authorization Header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
logger.info("=> JWT from header, {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
logger.info("=> JWT header null");
            return null;
        }

        String token = authHeader.substring(7);

        // Extract username from token
        String username = jwtService.extractTokenUsername(token);
logger.info("=> Username from token, {}", username);
        // Check if user exists in DB
        IfteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
logger.info("=> User details from DB, {}", user.toString());
        // Check if token is valid
        if (jwtService.isValidRefreshToken(token, user)) {
logger.info("=> JWT is a valid refresh token, refreshing...");
            // Generate access token
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);
logger.info("=> All JWT revoked and new ones were saved");
            return new IfteAuthenticationResponse(accessToken, refreshToken, "New token generated");
        }
logger.info("=> JTW not a valid refresh token");
        return null;
    }

    private void revokeAllTokenByUser(IfteUser user) {
        List<IfteToken> validTokens = tokenRepository.findAllNotLoggedOutTokensByUserId(user.getId());

        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(token -> token.setLoggedOut(true));

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, IfteUser user) {
        IfteToken token = new IfteToken();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);

        tokenRepository.save(token);
    }
}