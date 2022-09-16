package com.company.springbootapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/example")
public class MyRestController {

    @GetMapping
    public ModelAndView example() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("example");
        return modelAndView;
    }


}
