package co.jht.config;

import co.jht.enums.UserRole;
import co.jht.enums.UserStatus;
import co.jht.model.domain.persist.appuser.AppUser;
import co.jht.repository.UserRepository;
import co.jht.util.DateTimeFormatterUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.first-user.name}")
    private String username;

    @Value("${app.first-user.password}")
    private String userPassword;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            createUser(username, userPassword, "admin@tda.com", "FirstAdmin", "LastAdmin", UserRole.ADMIN);
            createUser("regularUser", "regularPass", "regular@user.com", "FirstUser", "LastUser", UserRole.USER);
        }
    }

    private void createUser(
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            UserRole role
    ) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setProfilePictureUrl("http://example.com/profile.jpg");
        user.setRegistrationDate(DateTimeFormatterUtil.getCurrentTokyoTime());
        user.setAccountStatus(UserStatus.ACTIVE);
        user.setRole(role);
        user.setVersion(0L);

        userRepository.save(user);
    }
}