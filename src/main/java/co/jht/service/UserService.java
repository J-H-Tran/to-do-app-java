package co.jht.service;

import co.jht.entity.AppUser;

public interface UserService {
    void registerUser(String username, String password, String email);
    AppUser findByUsername(String username);
    boolean authenticateUser(String username, String password);
}