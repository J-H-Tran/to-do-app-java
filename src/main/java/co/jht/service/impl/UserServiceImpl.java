package co.jht.service.impl;

import co.jht.entity.AppUser;
import co.jht.repository.UserRepository;
import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(String username, String password, String email) {
        AppUser appUser = new AppUser();

        appUser.setUsername(username);
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.setEmail(email);

        userRepository.save(appUser);
    }

    @Override
    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        AppUser appUser = userRepository.findByUsername(username);
        return appUser != null && passwordEncoder.matches(password, appUser.getPassword());
    }

}