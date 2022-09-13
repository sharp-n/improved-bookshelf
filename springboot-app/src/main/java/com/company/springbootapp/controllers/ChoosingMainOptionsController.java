package com.company.springbootapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ChoosingMainOptionsController {

    @GetMapping("/login-spring")
    public String showLoginPage(Model model){
        return "login-spring";
    }

    @PostMapping("/login-spring")
    public String getUserName(@RequestParam("name") String name, Model model) {
        //todo implement
        return "redirect:/file-work";
    }

    @GetMapping("/file-work")
    public String choosingFileWork(Model model){
        return "file-work-choose";
    }

    @PostMapping("/file-work")
    public String getParams(@RequestParam("type-of-work") String typeOfWork, Model model){
        //todo implement
        return "redirect:/choose-item";
    }

    @GetMapping("/choose-item")
    public String showChooseItemPage(Model model){
        return "choose-item";
    }

    @PostMapping("/choose-item")
    public String getChosenItem(@RequestParam("type-of-item") String typeOfItem, Model model){
        //todo implement

        return "redirect:/choose-action";
    }

    @GetMapping
    public String redirectToLogin(Model model){
        return "redirect:/login-spring";
    }

}
