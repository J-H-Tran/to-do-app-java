package co.jht.controller;

import co.jht.entity.AppUser;
import co.jht.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to the home page!");
        return "welcome";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            Model model
    ) {
        // Add logic to handle registration (e.g., save user to the database)
        AppUser appUser = new AppUser();

        appUser.setUsername(username);
        appUser.setPassword(password);
        appUser.setEmail(email);

        userRepository.save(appUser);

        model.addAttribute("message", "Registration successful!");
        return "welcome";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        // Add logic to handle login (e.g., authenticate user)
        model.addAttribute("message", "Login successful!");
        return "welcome";
    }
}