package com.company.springbootapp.controllers;

import com.company.ParametersForWeb;
import com.company.springbootapp.constants.CookieNames;
import com.company.springbootapp.constants.BlocksNames;
import com.company.springbootapp.constants.ThymeleafVariables;
import com.company.springbootapp.handlers.ControllersHandler;
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
public class  ChooseActionController {

    ControllersHandler handler;
    CookieUtil cookieUtil;

    @GetMapping
    public String showChooseActionPage(Model model){
        handler.addAttribute(model,BlocksNames.CHOOSE_ACTION, BlocksNames.REF_TO_LOGIN_ITEM);
        return "choose-main-options";
    }

    @PostMapping
    public String redirectToAction(HttpServletRequest request, @RequestParam String action){
        if (action.equals("add")) {
            String typeOfItem = cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM);
            return handler.redirectionToAddPage(typeOfItem);
        } else {
            return "redirect:/choose-action/" + action;
        }
    }

    @GetMapping({"/add","/add/book","/add/comics"})
    public String addItem(HttpServletRequest request,Model model){
        String template = handler.getAddTemplateBySimpleCLassName(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM)); //todo const
        handler.addAttribute(model,template, BlocksNames.REF_TO_LOGIN_ITEM_ACTION);
        return "item-actions";
    }

    @PostMapping("/add/book" )
    public String showAddBookPage(HttpServletRequest request,
                                  @RequestParam(name = "book-title") String title,
                                  @RequestParam(name = "book-pages") String pages,
                                  @RequestParam(name = "author") String author,
                                  @RequestParam(name = "day") String day,
                                  @RequestParam(name = "month") String month,
                                  @RequestParam(name = "year") String year,
                                  Model model){
        List<String> itemOptions = new ArrayList<>(Arrays.asList(title,pages,author,day+ "." + month + "." + year));
        ParametersForWeb params = handler.genAndGetParams(request);
        Boolean success = handler.addItem(itemOptions, params);
        handler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @PostMapping("/add/comics")
    public String showAddComicsPage(HttpServletRequest request,
                                    @RequestParam(name = "comics-title") String title,
                                    @RequestParam(name = "comics-pages") String pages,
                                    @RequestParam(name = "publisher") String publisher,
                                    Model model){
        List<String> itemOptions = new ArrayList<>(Arrays.asList(title,pages,publisher));
        ParametersForWeb params = handler.genAndGetParams(request);

        Boolean success = handler.addItem(itemOptions, params);
        handler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @PostMapping("/add")
    public String showAddItemPage(HttpServletRequest request,
                                  @RequestParam(name = "items-title") String title,
                                  @RequestParam(name = "items-pages") String pages,
                                  Model model){
        List<String> itemOptions = new ArrayList<>(Arrays.asList(title,pages));
        ParametersForWeb params = handler.genAndGetParams(request);
        Boolean success = handler.addItem(itemOptions, params);
        handler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @GetMapping("/delete")
    public String showDeleteItemPage(Model model){
        handler.addAttribute(model,BlocksNames.DELETE_ITEM_FORM, BlocksNames.REF_TO_LOGIN_ITEM_ACTION);
        return "item-actions";
    }

    @PostMapping("/delete")
    public String deleteItem(HttpServletRequest request, @RequestParam(name = "delete-id") int id, Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        Boolean success = handler.deleteItem(params,id);
        handler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @GetMapping("/take")
    public String showBorrowItemPage(Model model){
        handler.addAttribute(model,BlocksNames.TAKE_ITEM_FORM, BlocksNames.REF_TO_LOGIN_ITEM_ACTION);
        return "item-actions";
    }

    @PostMapping("/take")
    public String borrowItem(HttpServletRequest request, @RequestParam(name = "take-id") int id, Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        Boolean success = handler.takeItem(params,id, true);
        handler.informAboutActionSuccess(model,success);
        return "/inform-page-template";
    }

    @GetMapping("/return")
    public String showReturnItemPage(Model model){
        handler.addAttribute(model,BlocksNames.RETURN_ITEM_FORM, BlocksNames.REF_TO_LOGIN_ITEM_ACTION);
        return "item-actions";
    }

    @PostMapping("/return")
    public String returnItem(HttpServletRequest request, @RequestParam(name = "return-id") int id, Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        Boolean success = handler.takeItem(params,id, false);
        handler.informAboutActionSuccess(model,success);
        return "/inform-page-template";
    }

    @GetMapping("/show")
    public String showItemsPage(HttpServletRequest request,Model model){
        String template = handler.getSortingTemplateByTypeOfItem(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        handler.addAttribute(model,template, BlocksNames.REF_TO_LOGIN_ITEM_ACTION);
        return "item-actions";
    }

    @PostMapping(value = "/show")
    public String showItems(HttpServletRequest request, @RequestParam(name = "comparator") String option, Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        model.addAttribute(ThymeleafVariables.MESSAGE, handler.showItems(params,option));
        return "inform-page-template";
    }

    @GetMapping("/show-all-the-items")
    public String showAllTheItemsPage(HttpServletRequest request, Model model){ //todo implement
        ParametersForWeb params = handler.genAndGetParams(request);
        model.addAttribute(ThymeleafVariables.MESSAGE, handler.showItems(params));
        return "inform-page-template";
    }

}
