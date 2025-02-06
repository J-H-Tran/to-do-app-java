package co.jht.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthUsernameUtil {

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;

        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            currentUsername = ((UserDetails) auth.getPrincipal()).getUsername();
        }

        if (currentUsername == null) {
            throw new UsernameNotFoundException("Username not found for current session!");
        }

        return currentUsername;
    }

}