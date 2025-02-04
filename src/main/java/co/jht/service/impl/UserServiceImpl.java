package co.jht.service.impl;

import co.jht.entity.AppUser;
import co.jht.repository.UserRepository;
import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser registerUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }

    @Override
    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}