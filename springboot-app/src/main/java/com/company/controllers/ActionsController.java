package com.company.controllers;

import com.company.ParametersForWeb;
import com.company.handlers.ControllersHandler;
import com.company.springappconstants.CookieNames;
import com.company.springappconstants.BlocksNames;
import com.company.springappconstants.ThymeleafVariables;
import com.company.utils.CookieUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
@AllArgsConstructor
@RequestMapping("/choose-action")
public class ActionsController {

    ControllersHandler handler;
    CookieUtil cookieUtil;

    @GetMapping
    public String showChooseActionPage(Model model){
        handler.addAttribute(model,BlocksNames.CHOOSE_ACTION, BlocksNames.REF_TO_LOGIN_SHOW_ALL_ITEMS);
        return "choose-action";
    }

    @PostMapping
    public String redirectToAction(HttpServletRequest request, @RequestParam String action){
            return "redirect:/choose-action/" + action;
    }

    @GetMapping({"/add"})
    public String addItemPage(HttpServletRequest request,Model model){
        String template = handler.getAddTemplateBySimpleCLassName(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        handler.addAttribute(model,template, BlocksNames.REF_TO_LOGIN_SHOW_ALL_ITEMS);
        return "item-actions";
    }


    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addItem(HttpServletRequest request,
                                  @RequestBody String item,
                                  Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        Boolean success = handler.addItem(item, params);
        handler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @GetMapping("/delete")
    public String showDeleteItemPage(Model model){
        handler.addAttribute(model,BlocksNames.DELETE_ITEM_FORM, BlocksNames.REF_TO_LOGIN_ACTION);
        return "item-actions";
    }

    @PostMapping(value = "/delete")
    public String deleteItem(HttpServletRequest request, @RequestParam(name = "item_id") int id, Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        Boolean success = handler.deleteItem(params,id);
        handler.informAboutActionSuccess(model,success);
        return "inform-page-template";
    }

    @GetMapping("/take")
    public String showBorrowItemPage(Model model){
        handler.addAttribute(model,BlocksNames.TAKE_ITEM_FORM, BlocksNames.REF_TO_LOGIN_ACTION);
        return "item-actions";
    }

    @PostMapping("/take")
    public String borrowItem(HttpServletRequest request, @RequestParam(name = "item_id") int id, Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        Boolean success = handler.takeItem(params,id, true);
        handler.informAboutActionSuccess(model,success);
        return "/inform-page-template";
    }

    @GetMapping("/return")
    public String showReturnItemPage(Model model){
        handler.addAttribute(model,BlocksNames.RETURN_ITEM_FORM, BlocksNames.REF_TO_LOGIN_ACTION);
        return "item-actions";
    }

    @PostMapping("/return")
    public String returnItem(HttpServletRequest request, @RequestParam(name = "item_id") int id, Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        Boolean success = handler.takeItem(params,id, false);
        handler.informAboutActionSuccess(model,success);
        return "/inform-page-template";
    }

    @GetMapping("/show")
    public String showItemsPage(HttpServletRequest request,Model model){
        String template = handler.getSortingTemplateByTypeOfItem(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        handler.addAttribute(model,template, BlocksNames.REF_TO_LOGIN_ACTION);
        return "item-actions";
    }

    @PostMapping(value = "/show")
    public String showItems(HttpServletRequest request, @RequestParam(name = "sorting-param") String comparator, Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        model.addAttribute(ThymeleafVariables.MESSAGE, handler.showItems(params,comparator));
        return "inform-page-template";
    }

    @GetMapping("/show-all-the-items")
    public String showAllTheItemsPage(HttpServletRequest request, Model model){
        ParametersForWeb params = handler.genAndGetParams(request);
        model.addAttribute(ThymeleafVariables.MESSAGE, handler.showItems(params));
        return "inform-page-template";
    }

}