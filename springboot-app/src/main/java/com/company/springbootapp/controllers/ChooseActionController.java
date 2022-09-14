package com.company.springbootapp.controllers;

import com.company.springbootapp.handlers.ChooseActionsHandler;
import com.company.springbootapp.handlers.ChooseMainOptionsHandler;
import com.company.springbootapp.utils.MainParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/choose-action")
public class ChooseActionController {


    @GetMapping
    public String showChooseActionPage(Model model){

        model.addAttribute("form","choose-action");
        model.addAttribute("ref_template","refs-to-login-item");
        return "choose-main-options";
    }

    @GetMapping("/add")
    public String showAddItemPage(Model model){
        return "item-actions";
    }

    @PostMapping("/add")
    public String addItem(){
        //todo implementation
        return "inform-page-template";
    }

    @GetMapping("/delete")
    public String showDeleteItemPage(Model model){

        return "item-actions";
    }

    @PostMapping("/delete")
    public String deleteItem(){
        //todo implementation

        return "inform-page-template";
    }

    @GetMapping("/take")
    public String showBorrowItemPage(Model model){
        return "item-actions";
    }

    @PostMapping("/take")
    public String borrowItem(){
        //todo implementation
        return "/inform-page-template";
    }

    @GetMapping("/return")
    public String showReturnItemPage(Model model){
        return "item-actions";
    }

    @PostMapping("/return")
    public String returnItem(){
        //todo implementation
        return "/inform-page-template";
    }

    @GetMapping("/show")
    public String showItemsPage(Model model){
        return "item-actions";
    }

    @PostMapping("/show")
    public String showItems(Model model){
        //todo implementation
        return "/inform-page-template";
    }

    @GetMapping("/show-all-the-items")
    public String showAllTheItemsPage(Model model){
        return "item-actions";
    }

}
