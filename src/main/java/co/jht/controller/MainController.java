package co.jht.controller;

import co.jht.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MainController(
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/home")
    public String showHome(Model model) {
        model.addAttribute("message", "Welcome to the home page!");
        return "home";
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
            RedirectAttributes redirectAttributes
    ) {
        userService.registerUser(username, password, email);
        redirectAttributes.addFlashAttribute("message", "Registration successful!");
        return "redirect:/register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ) {
        if (userService.authenticateUser(username, password)) {
            return "redirect:/home";
        } else {
            redirectAttributes.addFlashAttribute("message", "Invalid username or password!");
            return "redirect:/login";
        }
    }
}