package co.jht.service;

import co.jht.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void registerUser(String username, String password, String email);
    AppUser findByUsername(String username);
    String authenticateUser(String username, String password);
}