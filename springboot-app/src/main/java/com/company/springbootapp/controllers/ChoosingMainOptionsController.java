package com.company.springbootapp.controllers;

import com.company.springbootapp.handlers.ChooseMainOptionsHandler;
import com.company.springbootapp.utils.CookieUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ChoosingMainOptionsController {

    ChooseMainOptionsHandler mainOptionsHandler;
    CookieUtil cookieUtil;

    @GetMapping("/login-spring")
    public String showLoginPage(Model model){
        model.addAttribute("form","login");
        model.addAttribute("ref_template","no-refs");
        return "choose-main-options";
    }

    @RequestMapping(value = "/login-spring", method = RequestMethod.POST,consumes = {"*/*"})
    public String getUserName(HttpServletResponse response, @RequestBody String name, Model model) {
        cookieUtil.createCookie(response,"userName",name);
        return "redirect:/file-work";
    }

    @GetMapping("/file-work")
    public String choosingFileWork(Model model) {
        model.addAttribute("form","file-work-choose");
        model.addAttribute("ref_template","change-user");
        return "choose-main-options";
    }

    @PostMapping("/file-work")
    public String getParams(HttpServletResponse response, @RequestParam("type-of-work") String typeOfWork, Model model){
        cookieUtil.createCookie(response,"typeOfWork",typeOfWork);
        return "redirect:/choose-item";
    }

    @GetMapping("/choose-item")
    public String showChooseItemPage(Model model){
        model.addAttribute("form","choose-item");
        model.addAttribute("ref_template","refs-to-show-login-item");
        return "choose-main-options";
    }

    @PostMapping("/choose-item")
    public String getChosenItem(HttpServletResponse response, @RequestParam("type-of-item") String typeOfItem, Model model){
        cookieUtil.createCookie(response,"typeOfItem",typeOfItem);
        return "redirect:/choose-action";
    }

    @GetMapping
    public String redirectToLogin(Model model){
        return "redirect:/login-spring";
    }

}
