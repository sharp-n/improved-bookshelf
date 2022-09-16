package com.company.springbootapp.handlers;

import com.company.Item;
import com.company.User;
import com.company.WebAppService;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.ParametersForWeb;
import com.company.springbootapp.constants.CookieNames;
import com.company.springbootapp.constants.MessagesAndTitlesConstants;
import com.company.springbootapp.utils.CookieUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ChooseActionsHandler {

    Map<String,String> typeOfItemTemplate = new HashMap<>();
    Map<String,String> typeOfItemRef = new HashMap<>();
    CookieUtil cookieUtil;
    public String getAddTemplateBySimpleCLassName(String typeOfItem){ //todo refactor map init
        typeOfItemTemplate.put("book","add-book-form");
        typeOfItemTemplate.put("comics","add-comics-form");
        typeOfItemTemplate.put("default","add-main-option-items-form");
        for (Map.Entry<String,String> typeOfItemTemplateEntry : typeOfItemTemplate.entrySet()) {
            if (typeOfItemTemplateEntry.getKey().equalsIgnoreCase(typeOfItem)){
                return typeOfItemTemplateEntry.getValue();
            }
        }
        return typeOfItemTemplate.get("default");
    }

    public String redirectionToAddPage(String typeOfItem) { // todo optimize (include getTemplateBySimpleCLassName())
        typeOfItemRef.put("book","/add/book"); // todo refactor map init
        typeOfItemRef.put("comics","/add/comics");
        typeOfItemRef.put("default","/add");
        for (Map.Entry<String,String> typeOfItemRefEntry : typeOfItemRef.entrySet()) {
            System.out.println(typeOfItemRefEntry.getKey());
            if (typeOfItemRefEntry.getKey().equalsIgnoreCase(typeOfItem)){
                System.out.println(typeOfItemRefEntry.getValue());
                return "redirect:/choose-action" + typeOfItemRefEntry.getValue();
            }
        }
        System.out.println(typeOfItemRef.get("default"));
        return "redirect:/choose-action" + typeOfItemRef.get("default");
    }


    public String getSortingTemplateByTypeOfItem(String typeOfItem) { // todo optimize
        typeOfItemTemplate.put("book","books-sorting-options-form");
        typeOfItemTemplate.put("comics","comics-sorting-options-form");
        typeOfItemTemplate.put("default","items-sorting-options-form");
        for (Map.Entry<String,String> typeOfItemTemplateEntry : typeOfItemTemplate.entrySet()) {
            if (typeOfItemTemplateEntry.getKey().equalsIgnoreCase(typeOfItem)){
                return typeOfItemTemplateEntry.getValue();
            }
        }
        return typeOfItemTemplate.get("default");
    }

    public Boolean addItem(List<String> itemOptions, ParametersForWeb params) {
        try {
            ProjectHandler projectHandler = initProjectHandler(params);
            ItemHandler itemHandler = projectHandler.getItemHandler();
            return projectHandler.getLibrarian().addItem(itemHandler, itemOptions);
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteItem(ParametersForWeb params, int id) {
        try {
            ProjectHandler projectHandler = initProjectHandler(params);
            return projectHandler.getLibrarian().deleteItem(id, false);
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public ProjectHandler initProjectHandler(ParametersForWeb params){
        String typeOfItem = params.getTypeOfItem();
        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out)); // todo optimize handlers
        projectHandler.itemMenuSwitch(MainMenu.getByOption(typeOfItem));
        FilesMenu option = FilesMenu.getByDBColumnName(params.getTypeOfFileWork());
        projectHandler.fileSwitch(option, new User(params.getName()));
        return projectHandler;
    }

    public void informAboutActionSuccess(Model model, boolean success) {
        model.addAttribute("message", MessagesAndTitlesConstants.successFailMessageMap.get(success));
        model.addAttribute("title",MessagesAndTitlesConstants.successFailTitleMap.get(success));
    }

    public Boolean takeItem(ParametersForWeb params, int id, boolean forBorrow) {
        try {
            ProjectHandler projectHandler = initProjectHandler(params);
            return projectHandler.getLibrarian().borrowItem(id, forBorrow);
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public String showItems(ParametersForWeb params, String option) {
        try {

            // todo refactor
            return new WebAppService().genTableOfSortedItemsFromFiles(params,option);
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    public String showItems(ParametersForWeb params) {
        try {
            return new WebAppService().genTableOfSortedItemsFromFiles(params,SortingMenu.ITEM_ID.getOption()); // or from db
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    public ParametersForWeb genAndGetParams(HttpServletRequest request) {
        ParametersForWeb params = new ParametersForWeb();
        params.setName(cookieUtil.getCookies(request).get(CookieNames.USER_NAME));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfItem(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        return params;
    }

    public void addAttribute(Model model, String form, String refTemplate) {
        model.addAttribute("form",form);
        model.addAttribute("ref_template",refTemplate);
    }
}
