package com.company.controllers;

import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class MyErrorController implements ErrorController {

    private static final Logger log
            = Logger.getLogger(ErrorController.class);

    @GetMapping
    public String handleError(HttpServletRequest request, Model model) {
        model.addAttribute("ERROR_CODE",request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        model.addAttribute("ERROR_MESSAGE",request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        log.error(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) + " : " + request.getAttribute(RequestDispatcher.ERROR_MESSAGE) );
        return "error";
    }

}