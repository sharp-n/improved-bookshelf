package com.company.springbootapp.controllers;

import com.company.HTMLFormBuilder;
import com.company.URLConstants;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/choose-action")
public class ChooseActionController {

    @GetMapping
    public String showChooseActionPage(Model model){
        return "actions-template";
    }

    @GetMapping("/add")
    public String showAddItemPage(Model model){
        //String formContent = new HTMLFormBuilder().genForm(ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassBySimpleNameOfClass("Book")).genAddFormContent(), URLConstants.ADD_PAGE);

        //model.addAttribute("FORM",formContent);
        return "actions/actions-realization-template";
    }

    @PostMapping("/add")
    public String addItem(){
        return "actions/inform-page-template";
    }


}
