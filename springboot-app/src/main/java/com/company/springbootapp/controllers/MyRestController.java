package com.company.springbootapp.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


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
