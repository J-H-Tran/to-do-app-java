package co.jht.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthUserUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserUtil.class);

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
        SecurityContext context = SecurityContextHolder.getContext();
        logger.info("Context retrieved from SecurityContextHolder: {}", context.toString());

        Authentication auth = context.getAuthentication();
        logger.info("Authentication retrieved from SecurityContextHolder: {}", auth);

        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            String authUsername = ((UserDetails) auth.getPrincipal()).getUsername();
            logger.info("Context username: {}", authUsername);
            return authUsername;
        }
        return null;
    }

    public static Collection<? extends GrantedAuthority> getAuthUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities();
    }
}