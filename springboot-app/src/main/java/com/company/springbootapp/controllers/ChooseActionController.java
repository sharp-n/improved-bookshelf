package com.company.springbootapp.controllers;

import com.company.parameters.ParametersFromURL;
import com.company.springbootapp.constants.CookieNames;
import com.company.springbootapp.handlers.ChooseActionsHandler;
import com.company.springbootapp.utils.CookieUtil;
import com.company.springbootapp.utils.MainParams;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Controller
@AllArgsConstructor
@RequestMapping("/choose-action")
public class  ChooseActionController {

    ChooseActionsHandler actionsHandler;
    CookieUtil cookieUtil;

    @GetMapping
    public String showChooseActionPage(Model model){
        model.addAttribute("form","choose-action");
        model.addAttribute("ref_template","refs-to-login-item");
        return "choose-main-options";
    }

    @PostMapping
    public String redirectToAction(HttpServletRequest request, @RequestParam String action, Model model){
        if (action.equals("add")) {
            String typeOfItem = cookieUtil.getCookies(request).get("typeOfItem");
            return actionsHandler.redirectionToAddPage(typeOfItem);
        } else {
            return "redirect:/choose-action/" + action;
        }
    }

    @GetMapping({"/add","/add/book","/add/comics"})
    public String addItem(HttpServletRequest request,Model model){
        String template = actionsHandler.getAddTemplateBySimpleCLassName(cookieUtil.getCookies(request).get("typeOfItem"));
        model.addAttribute("form",template);
        model.addAttribute("ref_template","refs-to-login-item-action");
        return "item-actions";
    }

    @PostMapping("/add/book")
    public String showAddBookPage(HttpServletRequest request,
                                  @RequestParam(name = "book-title") String title,
                                  @RequestParam(name = "book-pages") String pages,
                                  @RequestParam(name = "author") String author,
                                  @RequestParam(name = "day") String day,
                                  @RequestParam(name = "month") String month,
                                  @RequestParam(name = "year") String year,
                                  Model model){
        List<String> itemOptions = new ArrayList<>(Arrays.asList(title,pages,author,day+ "." + month + "." + year));
        MainParams params = new MainParams();
        params.setName(cookieUtil.getCookies(request).get("typeOfItem"));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        Boolean success = actionsHandler.addItem(itemOptions, params);
        actionsHandler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @PostMapping("/add/comics")
    public String showAddComicsPage(HttpServletRequest request,
                                    @RequestParam(name = "comics-title") String title,
                                    @RequestParam(name = "comics-pages") String pages,
                                    @RequestParam(name = "publisher") String publisher,
                                    Model model){
        List<String> itemOptions = new ArrayList<>(Arrays.asList(title,pages,publisher));
        MainParams params = new MainParams();
        params.setName(cookieUtil.getCookies(request).get("typeOfItem"));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        Boolean success = actionsHandler.addItem(itemOptions, params);
        actionsHandler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @PostMapping("/add")
    public String showAddItemPage(HttpServletRequest request,
                                  @RequestParam(name = "items-title") String title,
                                  @RequestParam(name = "items-pages") String pages,
                                  Model model){ // todo implement
        List<String> itemOptions = new ArrayList<>(Arrays.asList(title,pages));
        MainParams params = new MainParams();
        params.setName(cookieUtil.getCookies(request).get("typeOfItem"));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        Boolean success = actionsHandler.addItem(itemOptions, params);
        actionsHandler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @GetMapping("/delete")
    public String showDeleteItemPage(Model model){
        model.addAttribute("form","delete-item-form");
        model.addAttribute("ref_template","refs-to-login-item-action");
        return "item-actions";
    }

    @PostMapping("/delete")
    public String deleteItem(HttpServletRequest request, @RequestParam(name = "delete-id") int id, Model model){
        MainParams params = new MainParams();
        params.setName(cookieUtil.getCookies(request).get("typeOfItem"));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        Boolean success = actionsHandler.deleteItem(params,id);
        actionsHandler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @GetMapping("/take")
    public String showBorrowItemPage(Model model){
        model.addAttribute("form","take-item-form");
        model.addAttribute("ref_template","refs-to-login-item-action");
        return "item-actions";
    }

    @PostMapping("/take")
    public String borrowItem(HttpServletRequest request, @RequestParam(name = "take-id") int id, Model model){
        MainParams params = new MainParams();
        params.setName(cookieUtil.getCookies(request).get("typeOfItem"));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        Boolean success = actionsHandler.takeItem(params,id, true);
        actionsHandler.informAboutActionSuccess(model,success);
        return "/inform-page-template";
    }

    @GetMapping("/return")
    public String showReturnItemPage(Model model){
        model.addAttribute("form","return-item-form");
        model.addAttribute("ref_template","refs-to-login-item-action");
        return "item-actions";
    }

    @PostMapping("/return")
    public String returnItem(HttpServletRequest request, @RequestParam(name = "return-id") int id, Model model){
        MainParams params = new MainParams();
        params.setName(cookieUtil.getCookies(request).get("typeOfItem"));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        Boolean success = actionsHandler.takeItem(params,id, false);
        actionsHandler.informAboutActionSuccess(model,success);
        return "/inform-page-template";
    }

    @GetMapping("/show")
    public String showItemsPage(HttpServletRequest request,Model model){
        String template = actionsHandler.getSortingTemplateByTypeOfItem(cookieUtil.getCookies(request).get("typeOfItem"));
        model.addAttribute("form",template);
        model.addAttribute("ref_template","refs-to-login-item-action");
        return "item-actions";
    }

    @PostMapping(value = "/show")
    public String showItems(HttpServletRequest request, @RequestParam(name = "comparator") String option, Model model){
        ParametersFromURL params = new ParametersFromURL();
        params.setName(cookieUtil.getCookies(request).get("userName"));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfItem(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        model.addAttribute("message",actionsHandler.showItems(params,option));
        return "inform-page-template";
    }

    @GetMapping("/show-all-the-items")
    public String showAllTheItemsPage(HttpServletRequest request, Model model){ //todo implement
        MainParams mainParams = new MainParams();
        mainParams.setName(cookieUtil.getCookies(request).get(CookieNames.USER_NAME));
        mainParams.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        model.addAttribute("message",actionsHandler.showItems(mainParams));
        return "inform-page-template";
    }

}
