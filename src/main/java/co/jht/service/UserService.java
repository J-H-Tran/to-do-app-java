package co.jht.service;

import co.jht.entity.User;

public interface UserService {
    User registeruser(User user);
    User findByUsername(String username);
}