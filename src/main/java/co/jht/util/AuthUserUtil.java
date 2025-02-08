package co.jht.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public class AuthUserUtil {

    public static UsernamePasswordAuthenticationToken setAuthUserContext(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }

    public static String getAuthUsername() {
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

    public static Collection<? extends GrantedAuthority> getAuthUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities();
    }
}