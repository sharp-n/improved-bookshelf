package com.company.springbootapp.handlers;

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
import com.company.springbootapp.constants.TemplatesAndRefs;
import com.company.springbootapp.utils.CookieUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Scanner;

import static com.company.springbootapp.constants.ThymeleafVariables.*;
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ControllersHandler {

    CookieUtil cookieUtil;



    public String getAddTemplateBySimpleCLassName(String typeOfItem){ //todo refactor map init
        return TemplatesAndRefs.getByOptionType(typeOfItem).getAddForm();
    }

    public String redirectionToAddPage(String typeOfItem) {
        return "redirect:/choose-action" + TemplatesAndRefs.getByOptionType(typeOfItem).getRef();
    }

    public String getSortingTemplateByTypeOfItem(String typeOfItem) { // todo optimize
        return TemplatesAndRefs.getByOptionType(typeOfItem).getSortingForm();
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
        return new WebAppService().getTable(SortingMenu.ITEM_ID.getOption(),initProjectHandler(params),params);
    }

    public ParametersForWeb genAndGetParams(HttpServletRequest request) {
        ParametersForWeb params = new ParametersForWeb();
        params.setName(cookieUtil.getCookies(request).get(CookieNames.USER_NAME));
        params.setTypeOfFileWork(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK));
        params.setTypeOfItem(cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_ITEM));
        return params;
    }

    public void addAttribute(Model model, String form, String refTemplate) {
        model.addAttribute(FORM,form);
        model.addAttribute(REF,refTemplate);
    }
}
