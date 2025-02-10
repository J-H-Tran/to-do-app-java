package co.jht.service;

import co.jht.model.domain.persist.appuser.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<AppUser> getAllUsers();
    AppUser getUserById(Long userId);
    AppUser createUser(AppUser user);
    AppUser updateUser(AppUser user, String username);
    void deleteUser(String username);
    AppUser findByUsername(String username);
    void registerUser(AppUser user);
    String authenticateUser(AppUser user);
    void logoutUser();
}