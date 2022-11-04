package com.company.handlers;

import com.company.*;
import com.company.controllers.ActionsController;
import com.company.db.services.ItemService;
import com.company.db.services.UserService;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.item_handlers.DefaultItemHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.springappconstants.CookieNames;
import com.company.springappconstants.MessagesAndTitlesConstants;
import com.company.enums.TemplatesAndRefs;
import com.company.table.HtmlTableBuilder;
import com.company.utils.CookieUtil;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.company.springappconstants.ThymeleafVariables.*;
@Service
@AllArgsConstructor
public class ControllersHandler {

    private static final Logger log
            = Logger.getLogger(ControllersHandler.class);


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
            if (checkTypeOFFileWork(params)) {
                Item item = new DefaultLibrarian().getItemFromJson(
                        jsonItem,
                        ItemHandlerProvider.getHandlerByClass(
                                ItemHandlerProvider.getClassBySimpleNameOfClass(params.getTypeOfItem())));
                return addItemToDB(item,params);
            } else {
                ProjectHandler projectHandler = initProjectHandler(params);
                ItemHandler itemHandler = projectHandler.getItemHandler();
                Item item = projectHandler.getLibrarian().getItemFromJson(jsonItem, itemHandler);
                item.setItemID(projectHandler.getLibrarian().genItemID());
                return projectHandler.getLibrarian().addItem(item);
            }
        } catch(IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }
    public Boolean addItemToDB(Item item, ParametersForWeb params){
        if(!userService.checkUserExistence(params.getName())){
            userService.addUser(params.getName());
        }
        return ItemHandlerProvider.getHandlerByClass(
                ItemHandlerProvider.getClassBySimpleNameOfClass(params.typeOfItem)).addItemToDB(item,params.getName(), itemService, userService);
    }

    public Boolean deleteItem(ParametersForWeb params, int id) {
        if (checkTypeOFFileWork(params)) {
            return delete(id, params);
        } else {
            ProjectHandler projectHandler = initProjectHandler(params);
            return projectHandler.getLibrarian().deleteItem(id, false);
        }
    }

    public boolean delete(int id, ParametersForWeb params){
        try {
            if (itemService.checkItemExistence(id) & params.getName().equals(itemService.getItemById(id).getUser().getName())) {
                itemService.removeItem(id);
                return true;
            }
            return false;
        } catch (NoSuchElementException noSuchElementException) {
            log.error(noSuchElementException.getMessage());
            return false;
        }
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
        if (checkTypeOFFileWork(params)) {
            return takeItemFromDB(id, forBorrow, params);
        } else {
            ProjectHandler projectHandler = initProjectHandler(params);
            return projectHandler.getLibrarian().borrowItem(id, forBorrow);
        }
    }

    public boolean takeItemFromDB(int id, boolean forBorrow, ParametersForWeb params){
        try {
            if (checkTypeOFFileWork(params)) {
                return itemService.updateBorrowed(id, forBorrow);
            }
            return false;
        } catch (NoSuchElementException noSuchElementException){
            log.error(noSuchElementException.getMessage());
            return false;
        }
    }

    public String showItems(ParametersForWeb params, String option) {
        try {
            if (checkTypeOFFileWork(params)){
                return genTableOfSortedItemsFromDB(params, option);
            }else {
                return new WebAppService().genTableOfSortedItemsFromFiles(params, option);
            }
        } catch (IOException ioException){
            ioException.printStackTrace();
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
        return params.getTypeOfWork().equals(FilesMenu.DATABASE_MYSQL.getServletParameter())||params.getTypeOfWork().equals(FilesMenu.DATABASE_SQLITE.getServletParameter());
    }


}
