package co.jht.model.domain.persist.appuser;

import co.jht.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static co.jht.enums.UserRole.ROLE_ADMIN;
import static co.jht.enums.UserRole.ROLE_GUEST;
import static co.jht.enums.UserRole.ROLE_MODERATOR;
import static co.jht.enums.UserRole.ROLE_USER;

@Entity
@Table(name = "app_user_table")
@Getter
@Setter
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserRole role = ROLE_USER;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_expire", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime verificationCodeExpiresAt;

    @Column(name = "enabled")
    private boolean enabled;

    @Version
    @Column(nullable = false)
    private Long version;

    public AppUser(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = switch (this.email.substring(this.email.indexOf('@'))) {
            case "@tda.com" -> ROLE_ADMIN;
            case "@mod.com" -> ROLE_MODERATOR;
            case "@guest.com" -> ROLE_GUEST;
            default -> ROLE_USER;
        };
    }

    public AppUser() {

    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        this.role = switch (this.email.substring(this.email.indexOf('@'))) {
            case "@tda.com" -> ROLE_ADMIN;
            case "@mod.com" -> ROLE_MODERATOR;
            case "@guest.com" -> ROLE_GUEST;
            default -> ROLE_USER;
        };
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(this.role.name());
        return Collections.singleton(grantedAuthority);
    }

    // Can use default impl for the below without needing to override
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", verificationCode='" + verificationCode + '\'' +
                ", verificationCodeExpiresAt=" + verificationCodeExpiresAt +
                ", enabled=" + enabled +
                ", version=" + version +
                '}';
    }
}