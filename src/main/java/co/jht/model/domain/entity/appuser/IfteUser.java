package co.jht.model.domain.entity.appuser;

import co.jht.enums.UserRole;
import co.jht.model.domain.entity.token.IfteToken;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static co.jht.enums.UserRole.ROLE_ADMIN;
import static co.jht.enums.UserRole.ROLE_GUEST;
import static co.jht.enums.UserRole.ROLE_MODERATOR;
import static co.jht.enums.UserRole.ROLE_USER;

@Getter
@Setter
@Entity
@Table(name = "user_table")
public class IfteUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<IfteToken> userTokens;

    @Version
    @Column(nullable = false)
    private Long version;

    public IfteUser(String firstName, String lastName, String username, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = switch (this.username.split("_")[0]) {
            case "admin" -> ROLE_ADMIN;
            case "mod" -> ROLE_MODERATOR;
            case "guest" -> ROLE_GUEST;
            default -> ROLE_USER;
        };
    }

    public IfteUser() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        this.role = switch (this.username.split("_")[0]) {
            case "admin" -> ROLE_ADMIN;
            case "mod" -> ROLE_MODERATOR;
            case "guest" -> ROLE_GUEST;
            default -> ROLE_USER;
        };
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(this.role.name());
        return Collections.singleton(grantedAuthority);
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
    public String toString() {
        return "IfteUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", userTokens=" + userTokens +
                ", version=" + version +
                '}';
    }
}