package com.company.controllers;

import com.company.auth.AuthService;
import com.company.db.repositories.UserRepository;
import com.company.handlers.ControllersHandler;
import com.company.springappconstants.CookieNames;
import com.company.springappconstants.BlocksNames;
import com.company.utils.CookieUtil;
import com.company.utils.Params;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class LoginController {

    private static final Logger log
            = Logger.getLogger(LoginController.class);

    UserRepository userRepository;
    ControllersHandler handler;
    CookieUtil cookieUtil;
    AuthService authService;

    @GetMapping("/login")
    public String showLoginPage(Model model){
        try{
            handler.addAttribute(model,BlocksNames.LOGIN, BlocksNames.NO_REFS);
            return "login";
        } catch (Exception e){
            log.error(e.getMessage() + ":" + LoginController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.TEXT_HTML_VALUE)
    public String getUserName(HttpServletResponse response, @RequestBody Params params, Model model) {
        try{
            cookieUtil.createCookie(response, CookieNames.USER_NAME,params.getName());
            cookieUtil.createCookie(response, CookieNames.TYPE_OF_FILE_WORK,params.getTypeOfWork());
            cookieUtil.createCookie(response, CookieNames.TYPE_OF_ITEM,params.getTypeOfItem());
            return "redirect:/choose-action";
        } catch (Exception e){
            log.error(e.getMessage() + ":" + LoginController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @GetMapping
    public String redirectToLogin(Model model){
        try {
            return "redirect:/login";
        } catch (Exception e){
            log.error(e.getMessage() + ":" + LoginController.class.getSimpleName());
            return "redirect:/error";
        }
    }

}
