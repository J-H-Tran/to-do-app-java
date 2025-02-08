package co.jht.config;

import co.jht.enums.UserRole;
import co.jht.enums.UserStatus;
import co.jht.model.domain.persist.appuser.AppUser;
import co.jht.model.domain.persist.tasks.TaskItem;
import co.jht.repository.TaskRepository;
import co.jht.repository.UserRepository;
import co.jht.util.DateTimeFormatterUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.first-user.name}")
    private String username;

    @Value("${app.first-user.password}")
    private String userPassword;

    @Autowired
    public DataInitializer(
            UserRepository userRepository,
            TaskRepository taskRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            createUser(username, userPassword, "admin@tda.com", "FirstAdmin", "LastAdmin", UserRole.ROLE_ADMIN);
            createUser("regularUser", "regularPass", "regular@user.com", "FirstUser", "LastUser", UserRole.ROLE_USER);
            createUser("guestUser", "guestPass", "regular@guest.com", "FirstUser", "LastUser", UserRole.ROLE_GUEST);
        }

        if (taskRepository.count() == 0) {
            AppUser adminUser = userRepository.findByUsername(username);
            AppUser regularUser = userRepository.findByUsername("regularUser");
            AppUser guestUser = userRepository.findByUsername("guestUser");

            createTask("First Task", "Task description 1", DateTimeFormatterUtil.getCurrentTokyoTime(), null, false, regularUser);
            createTask("Second Task", "Task description 2", DateTimeFormatterUtil.getCurrentTokyoTime(), null, false, regularUser);
            createTask("Third Task", "Task description 3", DateTimeFormatterUtil.getCurrentTokyoTime(), null, false,
                    adminUser);
            createTask("Fourth Task", "Task description 4", DateTimeFormatterUtil.getCurrentTokyoTime(), null, false,
                    adminUser);
            createTask("Fifth Task", "Task description 5", DateTimeFormatterUtil.getCurrentTokyoTime(), null, false,
                    guestUser);
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

    private void createTask(
            String title,
            String description,
            ZonedDateTime creationDate,
            ZonedDateTime dueDate,
            boolean completeStatus,
            AppUser user
    ) {
        TaskItem task = new TaskItem();
        task.setTitle(title);
        task.setDescription(description);
        task.setCreationDate(creationDate);
        task.setDueDate(dueDate);
        task.setCompleteStatus(completeStatus);
        task.setUser(user);

        taskRepository.save(task);
    }
}