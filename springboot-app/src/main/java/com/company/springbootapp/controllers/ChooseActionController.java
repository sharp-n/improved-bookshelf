package com.company.springbootapp.controllers;

import com.company.springbootapp.handlers.ChooseActionsHandler;
import com.company.springbootapp.utils.CookieUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/choose-action")
public class ChooseActionController {

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
    public String showAddBookPage(@RequestParam(name = "book-title") String title,
                                  @RequestParam(name = "book-pages") String pages,
                                  @RequestParam(name = "author") String author,
                                  @RequestParam(name = "day") String day,
                                  @RequestParam(name = "month") String month,
                                  @RequestParam(name = "year") String year,
                                  Model model){
        List<String> itemOptions = new ArrayList<>(Arrays.asList(title,pages,author,day+ "." + month + "." + year));
        actionsHandler.addItem(itemOptions);
        return "inform-page-template";
    }

    @PostMapping("/add/comics")
    public String showAddComicsPage(@RequestParam(name = "comics-title") String title,
                                    @RequestParam(name = "comics-pages") String pages,
                                    @RequestParam(name = "publisher") String publisher,
                                    Model model){
        List<String> itemOptions = new ArrayList<>(Arrays.asList(title,pages,publisher));
        actionsHandler.addItem(itemOptions);
        // todo implement
        return "item-actions";
    }

    @PostMapping("/add")
    public String showAddItemPage(@RequestParam(name = "items-title") String title,
                                  @RequestParam(name = "items-pages") String pages,
                                  Model model){ // todo implement
        List<String> itemOptions = new ArrayList<>(Arrays.asList(title,pages));
        actionsHandler.addItem(itemOptions);
        return "item-actions";
    }


    @GetMapping("/delete")
    public String showDeleteItemPage(Model model){
        model.addAttribute("form","delete-item-form");
        model.addAttribute("ref_template","refs-to-login-item-action");
        return "item-actions";
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam(name = "delete-id") String id){
        //todo implementation

        return "inform-page-template";
    }

    @GetMapping("/take")
    public String showBorrowItemPage(Model model){
        model.addAttribute("form","take-item-form");
        model.addAttribute("ref_template","refs-to-login-item-action");
        return "item-actions";
    }

    @PostMapping("/take")
    public String borrowItem(@RequestParam(name = "take-id") String id){
        //todo implementation
        return "/inform-page-template";
    }

    @GetMapping("/return")
    public String showReturnItemPage(Model model){
        model.addAttribute("form","return-item-form");
        model.addAttribute("ref_template","refs-to-login-item-action");
        return "item-actions";
    }

    @PostMapping("/return")
    public String returnItem(@RequestParam(name = "return-id") String id){
        //todo implementation
        return "/inform-page-template";
    }

    @GetMapping("/show")
    public String showItemsPage(HttpServletRequest request,Model model){
        String template = actionsHandler.getSortingTemplateByTypeOfItem(cookieUtil.getCookies(request).get("typeOfItem"));
        model.addAttribute("form",template);
        model.addAttribute("ref_template","refs-to-login-item-action");
        return "item-actions";
    }

    @PostMapping("/show")
    public String showItems(@RequestParam(name = "comparator") String option,Model model){
        //todo implementation
        return "/inform-page-template";
    }

    @GetMapping("/show-all-the-items")
    public String showAllTheItemsPage(Model model){ //todo implement
        return "item-actions";
    }

}
