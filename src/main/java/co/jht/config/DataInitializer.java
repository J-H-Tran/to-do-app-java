package co.jht.config;

import co.jht.enums.UserRole;
import co.jht.enums.UserStatus;
import co.jht.model.domain.persist.entity.appuser.AppUser;
import co.jht.repository.UserRepository;
import co.jht.util.DateTimeFormatterUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static co.jht.constants.ApplicationConstants.ASIA_TOKYO;

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
            AppUser firstUser = new AppUser();

            firstUser.setUsername(username);
            firstUser.setPassword(passwordEncoder.encode(userPassword)); // Make sure to encode the password
            firstUser.setEmail("admin@tda.com");
            firstUser.setFirstName("FirstAdmin");
            firstUser.setLastName("LastAdmin");
            firstUser.setProfilePictureUrl("http://example.com/profile.jpg");
            firstUser.setRegistrationDate(ZonedDateTime.parse(ZonedDateTime.now(ZoneId.of(ASIA_TOKYO)).format(DateTimeFormatterUtil.getFormatter())));
            firstUser.setAccountStatus(UserStatus.ACTIVE);
            firstUser.setRole(UserRole.ADMIN);
            firstUser.setVersion(0L);

            userRepository.save(firstUser);
        }
    }
}