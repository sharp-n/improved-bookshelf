package com.company.springbootapp.controllers;

import com.company.enums.springappconstants.CookieNames;
import com.company.enums.springappconstants.BlocksNames;
import com.company.springbootapp.handlers.ControllersHandler;
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

    ControllersHandler handler;
    CookieUtil cookieUtil;

    @GetMapping("/login-spring")
    public String showLoginPage(Model model){
        handler.addAttribute(model,BlocksNames.LOGIN, BlocksNames.NO_REFS);
        return "choose-main-options";
    }

    @RequestMapping(value = "/login-spring", method = RequestMethod.POST,consumes = {"*/*"})
    public String getUserName(HttpServletResponse response, @RequestBody String name, Model model) {
        cookieUtil.createCookie(response,CookieNames.USER_NAME,name);
        return "redirect:/file-work";
    }

    @GetMapping("/file-work")
    public String choosingFileWork(Model model) {
        handler.addAttribute(model,BlocksNames.FILE_WORK_CHOOSE, BlocksNames.REF_CHANGE_USER);
        return "choose-main-options";
    }

    @PostMapping("/file-work")
    public String getParams(HttpServletResponse response, @RequestParam("type-of-work") String typeOfWork, Model model){
        cookieUtil.createCookie(response,CookieNames.TYPE_OF_FILE_WORK,typeOfWork);
        return "redirect:/choose-item";
    }

    @GetMapping("/choose-item")
    public String showChooseItemPage(Model model){
        handler.addAttribute(model,BlocksNames.CHOOSE_ITEM, BlocksNames.REF_TO_SHOW_LOGIN_ITEM);
        return "choose-main-options";
    }

    @PostMapping("/choose-item")
    public String getChosenItem(HttpServletResponse response, @RequestParam("type-of-item") String typeOfItem, Model model){
        cookieUtil.createCookie(response, CookieNames.TYPE_OF_ITEM,typeOfItem);
        return "redirect:/choose-action";
    }

    @GetMapping
    public String redirectToLogin(Model model){
        return "redirect:/login-spring";
    }

}
