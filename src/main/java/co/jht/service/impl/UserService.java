//package co.jht.service.impl;
//
//import co.jht.model.domain.entity.appuser.AppUser;
//import co.jht.repository.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class UserService {
//    private final UserRepository userRepository;
//    public UserService(UserRepository userRepository, EmailService emailService) {
//        this.userRepository = userRepository;
//    }
//
//    public List<AppUser> allUsers() {
//        List<AppUser> users = new ArrayList<>();
//        userRepository.findAll().forEach(users::add);
//        return users;
//    }
//}