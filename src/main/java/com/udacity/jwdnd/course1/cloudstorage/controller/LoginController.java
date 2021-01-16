package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.LoginForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/login")
public class LoginController {
    UserService userService;

    public LoginController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String loginView(@ModelAttribute LoginForm loginForm) {
        return "login";
    }

    @PostMapping()
    public String login(@ModelAttribute LoginForm loginForm, Model model) {
        String resultRoute = "";
        System.out.println("Login form data: " + loginForm.getUsername());

        return "login";
    }
}
