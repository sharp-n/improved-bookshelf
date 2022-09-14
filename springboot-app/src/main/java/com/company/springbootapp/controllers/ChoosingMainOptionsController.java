package com.company.springbootapp.controllers;

import com.company.springbootapp.handlers.ChooseMainOptionsHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ChoosingMainOptionsController {

    ChooseMainOptionsHandler mainOptionsHandler;

    @GetMapping("/login-spring")
    public String showLoginPage(Model model){
        return "choose-main-options";
    }

    @PostMapping("/login-spring")
    public String getUserName(@RequestParam String name, Model model) {
        mainOptionsHandler.createUser(name); // todo check it
        return "redirect:/file-work";
    }

    @GetMapping("/file-work")
    public String choosingFileWork(Model model){
        return "file-work-choose";
    }

    @PostMapping("/file-work")
    public String getParams(@RequestParam("type-of-work") String typeOfWork, Model model){
        mainOptionsHandler.addTypeOfFileWork(typeOfWork);
        return "redirect:/choose-item";
    }

    @GetMapping("/choose-item")
    public String showChooseItemPage(Model model){
        return "choose-item";
    }

    @PostMapping("/choose-item")
    public String getChosenItem(@RequestParam("type-of-item") String typeOfItem, Model model){
        mainOptionsHandler.addTypeOfItem(typeOfItem);
        return "redirect:/choose-action";
    }

    @GetMapping
    public String redirectToLogin(Model model){
        return "redirect:/login-spring";
    }

}