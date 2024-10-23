package com.bikash.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bikash.entity.User;
import com.bikash.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid Credentials");
            return "login";
        }
        if (user.isAccountLocked()) {
            model.addAttribute("error", "Your Account Is Locked");
            return "login";
        }
        model.addAttribute("message", "Welcome to the application!");
        return "welcome";
    }

    @GetMapping("/register")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, Model model) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userService.saveUser(user);
        model.addAttribute("message", "Please check your email to unlock your account.");
        return "registration";
    }

    @GetMapping("/unlock-account")
    public String unlockAccountPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "unlock-account";
    }

    @PostMapping("/unlock-account")
    public String unlockAccount(@RequestParam String email, @RequestParam String tempPassword,
                                @RequestParam String newPassword, Model model) {
        User user = userService.findByEmail(email);
        if (user != null && passwordEncoder.matches(tempPassword, user.getPassword())) {
            userService.updatePassword(email, newPassword);
            model.addAttribute("message", "Account unlocked, please proceed with login.");
            return "login";
        } else {
            model.addAttribute("error", "Invalid temporary password.");
            return "unlock-account";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model model) {
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Email not registered.");
        } else {
            // Send email logic for the password (not implemented in this example)
            model.addAttribute("message", "Password sent to your email.");
        }
        return "forgot-password";
    }
}
