package co.jht.config;

import co.jht.enums.UserRole;
import co.jht.generator.TaskCodeGenerator;
import co.jht.model.domain.entity.appuser.AppUser;
import co.jht.model.domain.entity.appuser.IfteUser;
import co.jht.model.domain.entity.tasks.TaskItem;
import co.jht.repository.IfteUserRepository;
import co.jht.repository.TaskRepository;
import co.jht.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static co.jht.enums.UserRole.ROLE_ADMIN;
import static co.jht.enums.UserRole.ROLE_GUEST;
import static co.jht.enums.UserRole.ROLE_MODERATOR;
import static co.jht.enums.UserRole.ROLE_USER;

@Component
public class DataInitializer {

    private final UserRepository userRepositoryOld;
    private final IfteUserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskCodeGenerator taskCodeGenerator;
    private final JdbcTemplate jdbcTemplate;

    @Value("${app.first-user.name}")
    private String username;

    @Value("${app.first-user.password}")
    private String userPassword;

    @Autowired
    public DataInitializer(
            UserRepository userRepositoryOld,
            IfteUserRepository userRepository,
            TaskRepository taskRepository,
            PasswordEncoder passwordEncoder,
            TaskCodeGenerator taskCodeGenerator,
            JdbcTemplate jdbcTemplate
    ) {
        this.userRepositoryOld = userRepositoryOld;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskCodeGenerator = taskCodeGenerator;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS task_code_seq START 1");
        if (userRepository.count() == 0) {
            createNewUser("AdminFist", "AdminLast", username, userPassword, ROLE_ADMIN);
            createNewUser("ModFirst", "ModLast", "mod", "mod", ROLE_MODERATOR);
            createNewUser("UserFirst", "UserLast", "regular", "regular", ROLE_USER);
            createNewUser("GuestFirst", "GuestLast", "guest", "guest", ROLE_GUEST);
//            createUser(username, userPassword, "admin@tda.com", "FirstAdmin", "LastAdmin", UserRole.ROLE_ADMIN);
//            createUser("regularUser", "regularPass", "regular@user.com", "FirstUser", "LastUser", UserRole.ROLE_USER);
//            createUser("guestUser", "guestPass", "regular@guest.com", "FirstUser", "LastUser", UserRole.ROLE_GUEST);
        }

//        if (taskRepository.count() == 0) {
//            AppUser adminUser = userRepository.findByUsername(username).orElse(null);
//            AppUser regularUser = userRepository.findByUsername("regularUser").orElse(null);
//            AppUser guestUser = userRepository.findByUsername("guestUser").orElse(null);
//
//            createTask("First Task", "Task description 1", DateTimeFormatterUtil.getCurrentTokyoTime(), regularUser);
//            createTask("Second Task", "Task description 2", DateTimeFormatterUtil.getCurrentTokyoTime(), regularUser);
//            createTask("Third Task", "Task description 3", DateTimeFormatterUtil.getCurrentTokyoTime(), adminUser);
//            createTask("Fourth Task", "Task description 4", DateTimeFormatterUtil.getCurrentTokyoTime(), adminUser);
//            createTask("Fifth Task", "Task description 5", DateTimeFormatterUtil.getCurrentTokyoTime(), guestUser);
//        }
    }

    private void createNewUser(
            String firstName,
            String lastName,
            String username,
            String password,
            UserRole role
    ) {
        IfteUser user = new IfteUser(firstName, lastName, username, passwordEncoder.encode(password), role);
        user.setVersion(0L);

        userRepository.save(user);
    }

    private void createUser(
            String username,
            String password,
            String email
    ) {
        AppUser user = new AppUser(username, passwordEncoder.encode(password), email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setProfilePictureUrl("http://example.com/profile.jpg");
//        user.setRegistrationDate(DateTimeFormatterUtil.getCurrentTokyoTime());
//        user.setAccountStatus(UserStatus.ACTIVE);
//        user.setRole(role);
        user.setVersion(0L);

        userRepositoryOld.save(user);
    }

    private void createTask(
            String title,
            String description,
            ZonedDateTime creationDate,
            AppUser user
    ) {
        TaskItem task = new TaskItem();
        task.setTaskCode(taskCodeGenerator.generateTaskCode());
        task.setTitle(title);
        task.setDescription(description);
        task.setCreationDate(creationDate);
        task.setDueDate(null);
        task.setCompleteStatus(false);
        task.setUser(user);

        taskRepository.save(task);
    }
}