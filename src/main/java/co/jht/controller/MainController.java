package co.jht.controller;

import co.jht.entity.AppUser;
import co.jht.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home(Model model) {
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
            Model model
    ) {
        // Add logic to handle registration (e.g., save user to the database)
        AppUser appUser = new AppUser();

        appUser.setUsername(username);
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.setEmail(email);

        userRepository.save(appUser);

        model.addAttribute("message", "Registration successful!");
        return "home";
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
        AppUser appUser = userService.findByUsername(username);

        if (appUser != null && passwordEncoder.matches(password, appUser.getPassword())) {
            model.addAttribute("message", "Login successful!");
        } else {
            model.addAttribute("message", "Invalid username and password!");
            return "login";
        }
        model.addAttribute("message", "Login successful!");
        return "home";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Login successful!");
        return "redirect:/home";
    }
}