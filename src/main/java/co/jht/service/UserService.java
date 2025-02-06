package co.jht.service;

import co.jht.model.domain.persist.entity.appuser.AppUser;
import co.jht.model.domain.response.dto.appuser.AppUserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<AppUser> getAllUsers();
    AppUser getUserById(Long userId);
    AppUser createUser(AppUserDTO userDTO);
    AppUser updateUser(AppUser user);
    void deleteUser(Long userId);
    void registerUser(String username, String password, String email);
    AppUser findByUsername(String username);
    String authenticateUser(String username, String password);
}