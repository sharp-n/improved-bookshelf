package com.company.springapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login-spring")
    public String login(Model model){
        return "login-spring";
    }

    @PostMapping("/login-spring")
    public String getInfo(@RequestParam String name, Model model) {

        return "file-work-choose";
    }
}
