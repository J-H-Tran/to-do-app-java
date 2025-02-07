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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        // Convert roles to GrantedAuthority
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        // Initialize the user in the UserDetails
        return new User(user.getUsername(), user.getPassword(), Collections.singleton(grantedAuthority));
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
    public AppUser updateUser(AppUser user, String authUsername) {
        return userRepository.save(updateUserDetails(user, authUsername));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void registerUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(setCustomUserRole(user.getEmail()));

        userRepository.save(user);
    }

    @Override
    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public String authenticateUser(AppUser user) {
        AppUser userCreds = userRepository.findByUsername(user.getUsername());

        if (userCreds != null && passwordEncoder.matches(user.getPassword(), userCreds.getPassword())) {
            logger.info("User authenticated successfully: {}", userCreds.getUsername());
            return jwtTokenUtil.generateToken(userCreds.getUsername());
        }
        logger.error("Authentication failed for user: {}", user.getUsername());
        return null;
    }

    private UserRole setCustomUserRole(String emailDomain) {
        if (emailDomain.endsWith("@tda.com")) {
            return UserRole.ADMIN;
        }
        return UserRole.USER;
    }

    private AppUser updateUserDetails(AppUser user, String authUsername) {
        AppUser savedUser = userRepository.findByUsername(authUsername);
        savedUser.setUsername(user.getUsername());
        savedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        savedUser.setEmail(user.getEmail());
        savedUser.setFirstName(user.getFirstName());
        savedUser.setLastName(user.getLastName());
        savedUser.setProfilePictureUrl(user.getProfilePictureUrl());
        savedUser.setAccountStatus(user.getAccountStatus());

        return savedUser;
    }
}