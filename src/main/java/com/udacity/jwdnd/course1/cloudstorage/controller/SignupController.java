package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView () {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model) {

        if (!userService.isUsernameAvailable(user.getUsername())) {
            model.addAttribute("signupError", "This username is already in use. Please try another one.");
        } else {
            int rowsAdded = userService.createUser(user);

            if (rowsAdded < 0) {
                model.addAttribute("signupError", "There was an error with your request. Please try again later.");
            } else {
                model.addAttribute("signupSuccess", true);
                return "login";
            }
        }

        return "signup";
    }
}
