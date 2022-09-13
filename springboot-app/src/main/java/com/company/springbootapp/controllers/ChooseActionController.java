package com.company.springbootapp.controllers;

import com.company.springbootapp.handlers.ChooseActionsHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/choose-action")
public class ChooseActionController {

    ChooseActionsHandler actionsHandler;

    @GetMapping
    public String showChooseActionPage(Model model){
        return "/actions/actions-template";
    }

    @GetMapping("/add")
    public String showAddItemPage(Model model){
        return "/actions/add-item";
    }

    @PostMapping("/add")
    public String addItem(){
        //todo implementation
        return "/actions/inform-page-template";
    }

    @GetMapping("/delete")
    public String showDeleteItemPage(Model model){

        return "/actions/delete-item";
    }

    @PostMapping("/delete")
    public String deleteItem(){
        //todo implementation

        return "/actions/inform-page-template";
    }

    @GetMapping("/take")
    public String showBorrowItemPage(Model model){
        return "/actions/take-item";
    }

    @PostMapping("/take")
    public String borrowItem(){
        //todo implementation
        return "/actions/inform-page-template";
    }

    @GetMapping("/return")
    public String showReturnItemPage(Model model){
        return "/actions/return-item";
    }

    @PostMapping("/return")
    public String returnItem(){
        //todo implementation
        return "/actions/inform-page-template";
    }

    @GetMapping("/show")
    public String showItemsPage(Model model){
        return "/actions/show-items";
    }

    @PostMapping("/show")
    public String showItems(Model model){
        //todo implementation
        return "/actions/inform-page-template";
    }

}
