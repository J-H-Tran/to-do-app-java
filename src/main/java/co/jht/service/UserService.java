package co.jht.service;

import co.jht.entity.AppUser;

public interface UserService {
    AppUser registerUser(AppUser AppUser);
    AppUser findByUsername(String username);
}