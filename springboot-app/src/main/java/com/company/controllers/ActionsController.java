package com.company.controllers;

import com.company.ParametersForWeb;
import com.company.data_migration.DataMigrator;
import com.company.handlers.ControllersHandler;
import com.company.springappconstants.CookieNames;
import com.company.springappconstants.BlocksNames;
import com.company.springappconstants.ThymeleafVariables;
import com.company.utils.CookieUtil;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
@AllArgsConstructor
@RequestMapping("/choose-action")
public class ActionsController {

    private static final Logger log
            = Logger.getLogger(ActionsController.class);

    ControllersHandler handler;
    CookieUtil cookieUtil;

    @GetMapping
    public String showChooseActionPage(Model model){
        try {
            handler.addAttribute(model, BlocksNames.CHOOSE_ACTION, BlocksNames.REF_TO_LOGIN_SHOW_ALL_ITEMS);
            return "choose-action";
        } catch (Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @PostMapping
    public String redirectToAction(HttpServletRequest request, @RequestParam String action){
            return "redirect:/choose-action/" + action;
    }

    @GetMapping({"/add"})
    public String addItemPage(HttpServletRequest request,Model model){
        try{
            String template = handler.getAddTemplateBySimpleCLassName(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
            handler.addAttribute(model,template, BlocksNames.REF_TO_LOGIN_SHOW_ALL_ITEMS);
            return "item-actions";
        } catch(Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public String addItem(HttpServletRequest request,
                                  @RequestBody String item,
                                  Model model){
        try{
            ParametersForWeb params = handler.genAndGetParams(request);
            Boolean success = handler.addItem(item, params);
            new DataMigrator(params,handler).updateStorages();
            handler.informAboutActionSuccess(model,success);
            return "inform-page-template";
        } catch (Exception exception) {
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @GetMapping("/delete")
    public String showDeleteItemPage(Model model){
        try {
            handler.addAttribute(model, BlocksNames.DELETE_ITEM_FORM, BlocksNames.REF_TO_LOGIN_ACTION);
            return "item-actions";
        } catch(Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @PostMapping(value = "/delete")
    public String deleteItem(HttpServletRequest request, @RequestParam(name = "item_id") int id, Model model){
        try {
            ParametersForWeb params = handler.genAndGetParams(request);
            Boolean success = handler.deleteItem(params, id);
            new DataMigrator(params,handler).updateStorages();
            handler.informAboutActionSuccess(model, success);
            return "inform-page-template";
        } catch (Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @GetMapping("/take")
    public String showBorrowItemPage(Model model){
        try {
            handler.addAttribute(model, BlocksNames.TAKE_ITEM_FORM, BlocksNames.REF_TO_LOGIN_ACTION);
            return "item-actions";
        } catch (Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @PostMapping("/take")
    public String borrowItem(HttpServletRequest request, @RequestParam(name = "item_id") int id, Model model){
        try{
            ParametersForWeb params = handler.genAndGetParams(request);
            Boolean success = handler.takeItem(params,id, true);
            new DataMigrator(params,handler).updateStorages();
            handler.informAboutActionSuccess(model,success);
            return "/inform-page-template";
        } catch (Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }

    }

    @GetMapping("/return")
    public String showReturnItemPage(Model model){
        try{
            handler.addAttribute(model,BlocksNames.RETURN_ITEM_FORM, BlocksNames.REF_TO_LOGIN_ACTION);
            return "item-actions";
        } catch (Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @PostMapping("/return")
    public String returnItem(HttpServletRequest request, @RequestParam(name = "item_id") int id, Model model){
        try{
            ParametersForWeb params = handler.genAndGetParams(request);
            Boolean success = handler.takeItem(params,id, false);
            new DataMigrator(params,handler).updateStorages();
            handler.informAboutActionSuccess(model,success);
            return "/inform-page-template";
        } catch (Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }

    }

    @GetMapping("/show")
    public String showItemsPage(HttpServletRequest request,Model model){
        try {
            String template = handler.getSortingTemplateByTypeOfItem(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
            handler.addAttribute(model,template, BlocksNames.REF_TO_LOGIN_ACTION);
            return "item-actions";
        } catch (Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @PostMapping(value = "/show")
    public String showItems(HttpServletRequest request, @RequestParam(name = "sorting-param") String comparator, Model model){
        try {
            ParametersForWeb params = handler.genAndGetParams(request);
            model.addAttribute(ThymeleafVariables.MESSAGE, handler.showItems(params, comparator));
            return "inform-page-template";
        } catch (Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

    @GetMapping("/show-all-the-items")
    public String showAllTheItemsPage(HttpServletRequest request, Model model){
        try {
            ParametersForWeb params = handler.genAndGetParams(request);
            model.addAttribute(ThymeleafVariables.MESSAGE, handler.showItems(params));
            return "inform-page-template";
        } catch (Exception exception){
            log.error(exception.getMessage() + ":" + ActionsController.class.getSimpleName());
            return "redirect:/error";
        }
    }

}
