package com.company.springbootapp.handlers;

import com.company.Item;
import com.company.User;
import com.company.WebAppService;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.Librarian;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.parameters.ParametersFromURL;
import com.company.springbootapp.constants.CookieNames;
import com.company.springbootapp.constants.MessagesAndTitlesConstants;
import com.company.springbootapp.utils.CookieUtil;
import com.company.springbootapp.utils.MainParams;
import com.company.table.HtmlTableBuilder;
import com.mysql.cj.result.Field;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
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

    public boolean addItem(List<String> itemOptions, MainParams params) {
        try {
            ProjectHandler projectHandler = initProjectHandler(params);
            ItemHandler itemHandler = projectHandler.getItemHandler();
            return projectHandler.getLibrarian().addItem(itemHandler, itemOptions);
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(MainParams params, int id) {
        try {
            ProjectHandler projectHandler = initProjectHandler(params);
            return projectHandler.getLibrarian().deleteItem(id, false);
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public ProjectHandler initProjectHandler(MainParams params){
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

    public boolean takeItem(MainParams params, int id,boolean forBorrow) {
        try {
            ProjectHandler projectHandler = initProjectHandler(params);
            return projectHandler.getLibrarian().borrowItem(id, forBorrow);
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public String showItems(ParametersFromURL params, String option) {
        try {

            // todo refactor
            return new WebAppService().genTableOfSortedItemsFromFiles(params,option);
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    public String showItems(MainParams params) {
        try {
            return genTableOfSortedItemsFromFiles(params,SortingMenu.ITEM_ID.getOption());
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    public String genTableOfSortedItemsFromFiles(MainParams params, String sortingParam ) throws IOException {
        ProjectHandler projectHandler = initProjectHandler(params);
        List<List<String>> itemsAsStr = getItemsAsStringListSortedByComparator(projectHandler.getItemHandler(),projectHandler.getLibrarian(),sortingParam);
        return genTableOfSortedItems(projectHandler, itemsAsStr);
    }

    public String genTableOfSortedItems(ProjectHandler projectHandler, List<List<String>> itemsAsStr) {
        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(projectHandler.getItemHandler().getColumnTitles(), itemsAsStr);
        return tableBuilder.generateTable();
    }
    public List<List<String>> getItemsAsStringListSortedByComparator(ItemHandler itemHandler, Librarian librarian, String comparator) throws IOException {
        SortingMenu sortingParam = SortingMenu.getByOption(comparator);
        List<Item> items = librarian.initSortingItemsByComparator(sortingParam, itemHandler);
        return itemHandler.anyItemsToString(items);
    }

}
