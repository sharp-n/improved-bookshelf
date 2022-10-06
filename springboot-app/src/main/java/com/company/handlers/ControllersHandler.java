package com.company.handlers;

import com.company.Item;
import com.company.User;
import com.company.WebAppService;
import com.company.db.services.ItemService;
import com.company.db.services.UserService;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.item_handlers.DefaultItemHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.ParametersForWeb;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.springappconstants.CookieNames;
import com.company.springappconstants.MessagesAndTitlesConstants;
import com.company.enums.TemplatesAndRefs;
import com.company.table.HtmlTableBuilder;
import com.company.utils.CookieUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.company.springappconstants.ThymeleafVariables.*;
@Service
@AllArgsConstructor
public class ControllersHandler {

    CookieUtil cookieUtil;
    ItemService itemService;
    UserService userService;

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
            Item item = projectHandler.getLibrarian().getItemFromJson(jsonItem, itemHandler);

            if (checkTypeOFFileWork(params)) {
                return addItemToDB(item,params);
            } else {
                item.setItemID(projectHandler.getLibrarian().genItemID());
                return projectHandler.getLibrarian().addItem(item);
            }
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Boolean addItemToDB(Item item, ParametersForWeb params){
        if(!userService.checkUserExistence(Integer.parseInt(userService.getUserByName(params.getName()).getId().toString()))){
            userService.addUser(params.getName());
        }
        itemService.addItem(item,params.getName());
        return true;
    }

    public Boolean deleteItem(ParametersForWeb params, int id) {
        try {
            if (checkTypeOFFileWork(params)) {
                return delete(id,params);
            } else {
                ProjectHandler projectHandler = initProjectHandler(params);
                return projectHandler.getLibrarian().deleteItem(id, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id, ParametersForWeb params){
        if(itemService.checkItemExistence(id)&params.getName().equals(itemService.getItemById(id).getUser().getName())){
            itemService.removeItem(id);
            return true;
        }
        return false;
    }

    public ProjectHandler initProjectHandler(ParametersForWeb params){
        String typeOfItem = params.getTypeOfItem();
        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
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
            if(checkTypeOFFileWork(params)){
                return takeItemFromDB(id, forBorrow,params);
            } else{
                ProjectHandler projectHandler = initProjectHandler(params);
                return projectHandler.getLibrarian().borrowItem(id, forBorrow);
            }
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean takeItemFromDB(int id, boolean forBorrow, ParametersForWeb params){
        if(checkTypeOFFileWork(params)){
            return itemService.updateBorrowed(id,forBorrow);
        }
        return false;
    }

    public String showItems(ParametersForWeb params, String option) {
        try {
            if (checkTypeOFFileWork(params)){
                return genTableOfSortedItemsFromDB(params, option);
            }else {
                return new WebAppService().genTableOfSortedItemsFromFiles(params, option);
            }
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    private String genTableOfSortedItemsFromDB(ParametersForWeb params, String option) throws IOException {
        List<com.company.db.entities.Item> items;
        items = itemService.getAllElements().stream()
                .filter(item -> item.getUser().getName().equals(params.getName())&item.getTypeOfItem().equals(params.getTypeOfItem()))
                .collect(Collectors.toList());
        ItemHandler itemHandler = ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassBySimpleNameOfClass(params.getTypeOfItem()));
        List<Item> coreItems = itemHandler.convertToCoreDefinedTypeOfItems(items);
        return sortItemsAndBuildTable(coreItems,option,itemHandler);
    }

    public String showItems(ParametersForWeb params) {
        if (checkTypeOFFileWork(params)){
            List<com.company.db.entities.Item> items = itemService.getAllElements();
            ItemHandler itemHandler = new DefaultItemHandler();
            List<Item> coreItems = itemHandler.convertToCoreItems(items);
            return sortItemsAndBuildTable(coreItems,SortingMenu.ITEM_ID.getDbColumn(),itemHandler);
        }
        return new WebAppService().getTable(initProjectHandler(params),params);
    }

    public String sortItemsAndBuildTable(List<Item> coreItems, String option, ItemHandler itemHandler){
        Librarian librarian = new DefaultLibrarian();
        List<Item> sortedItems = librarian.sortItems(SortingMenu.getByParameter(option),itemHandler,coreItems);
        List<List<String>> itemsAsStr = itemHandler.itemsToString(sortedItems,itemHandler);
        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(itemHandler.getColumnTitles(), itemsAsStr);
        return tableBuilder.generateTable();
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

    public boolean checkTypeOFFileWork(ParametersForWeb params){
        return params.getTypeOfWork().equals(FilesMenu.DATABASE_MYSQL.getServletParameter());
    }


}
