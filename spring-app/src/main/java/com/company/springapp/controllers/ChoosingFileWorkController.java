package com.company.springapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChoosingFileWorkController {

    @GetMapping("/file-work")
    public String choosingFileWork(Model model){

        return "file-work-choose";
    }

    @PostMapping("/file-work")
    public String getParams(@RequestParam String typeOfWork, Model model){

        return "choose-item";
    }

}
