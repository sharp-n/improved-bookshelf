package com.company.handlers;

import com.company.Item;
import com.company.User;
import com.company.WebAppService;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.ParametersForWeb;
import com.company.springappconstants.CookieNames;
import com.company.springappconstants.MessagesAndTitlesConstants;
import com.company.enums.TemplatesAndRefs;
import com.company.utils.CookieUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Scanner;

import static com.company.springappconstants.ThymeleafVariables.*;
@Service
@AllArgsConstructor
public class ControllersHandler {

    CookieUtil cookieUtil;

    public String getAddTemplateBySimpleCLassName(String typeOfItem){
        return TemplatesAndRefs.getByOptionType(typeOfItem).getAddForm();
    }

    public String getSortingTemplateByTypeOfItem(String typeOfItem) {
        return TemplatesAndRefs.getByOptionType(typeOfItem).getSortingForm();
    }

    public Boolean addItem(String jsonItem, ParametersForWeb params) {
        try {
            ProjectHandler projectHandler = initProjectHandler(params);
            ItemHandler itemHandler = projectHandler.getItemHandler();
            Item item = projectHandler.getLibrarian().getItemFromJson(jsonItem,itemHandler);
            item.setItemID(projectHandler.getLibrarian().genItemID());
            return projectHandler.getLibrarian().addItem(item);
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
        FilesMenu option = FilesMenu.getByParameter(params.getTypeOfWork());
        projectHandler.fileSwitch(option, new User(params.getName()));
        return projectHandler;
    }

    public void informAboutActionSuccess(Model model, boolean success) {
        model.addAttribute(MESSAGE, MessagesAndTitlesConstants.successFailMessageMap.get(success));
        model.addAttribute(TITLE,MessagesAndTitlesConstants.successFailTitleMap.get(success));
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
            return new WebAppService().genTableOfSortedItemsFromFiles(params,option);
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    public String showItems(ParametersForWeb params) {
        return new WebAppService().getTable(initProjectHandler(params),params);
    }

    public ParametersForWeb genAndGetParams(HttpServletRequest request) {
        ParametersForWeb params = new ParametersForWeb();
        String name = cookieUtil.getCookies(request).get(CookieNames.USER_NAME);
        params.setName(name);
        params.setTypeOfWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfItem(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        return params;
    }

    public void addAttribute(Model model, String form, String refTemplate) {
        model.addAttribute(FORM,form);
        model.addAttribute(REF,refTemplate);
    }
}
