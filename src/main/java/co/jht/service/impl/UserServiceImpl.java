package co.jht.service.impl;

import co.jht.enums.UserRole;
import co.jht.model.domain.persist.appuser.AppUser;
import co.jht.repository.UserRepository;
import co.jht.security.jwt.JwtTokenUtil;
import co.jht.service.UserService;
import co.jht.util.DateTimeFormatterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenUtil jwtTokenUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // from UserDetailService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);

        if (user == null) {
            logger.error("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // Initialize the user in the UserDetail context
        return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public AppUser getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", userId);
                    return new UsernameNotFoundException("User not found with id: " + userId);
                });
    }

    @Transactional
    @Override
    public AppUser createUser(AppUser user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            logger.error("Password cannot be null for user: {}", user.getUsername());
            throw new IllegalArgumentException("Password cannot be null!");
        }
        user.setRegistrationDate(DateTimeFormatterUtil.getCurrentTokyoTime());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public AppUser updateUser(AppUser user) {
        return userRepository.save(updateUserDetails(user));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void registerUser(String username, String password, String email) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(setCustomUserRole(email));

        userRepository.save(user);
    }

    @Override
    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public String authenticateUser(String username, String password) {
        AppUser user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            logger.info("User authenticated successfully: {}", username);
            return jwtTokenUtil.generateToken(username);
        }
        logger.error("Authentication failed for user: {}", username);
        return null;
    }

    private UserRole setCustomUserRole(String emailDomain) {
        if (emailDomain.endsWith("@tda.com")) {
            return UserRole.ADMIN;
        }
        return UserRole.USER;
    }

    private AppUser updateUserDetails(AppUser user) {
        AppUser existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", user.getId());
                    return new IllegalArgumentException("User not found");
                });
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setProfilePictureUrl(user.getProfilePictureUrl());
        existingUser.setRegistrationDate(user.getRegistrationDate());
        existingUser.setAccountStatus(user.getAccountStatus());
        existingUser.setRole(user.getRole());

        return existingUser;
    }
}