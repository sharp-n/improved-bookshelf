package com.company;

import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.DBWorker;
import com.company.handlers.Librarian;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.parameters.ParametersFromURL;
import com.company.table.HtmlTableBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class WebAppService {

    public ProjectHandler genProjectHandlerFromParameters(ParametersFromURL param){
        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
        projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
        projectHandler.fileSwitch(FilesMenu.getByOption(param.typeOfFileWork), new User(param.name));
        return projectHandler;
    }

    public String genTableOfSortedItems(ProjectHandler projectHandler, List<List<String>> itemsAsStr) throws IOException {
        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(projectHandler.getItemHandler().getColumnTitles(), itemsAsStr);
        return tableBuilder.generateTable();
    }

    public String genTableOfSortedItemsFromFiles(ParametersFromURL param, String sortingParam ) throws IOException {
        ProjectHandler projectHandler = genProjectHandlerFromParameters(param);
        List<List<String>> itemsAsStr = getItemsAsStringListSortedByComparator(projectHandler.getItemHandler(),projectHandler.getLibrarian(),sortingParam);
        return genTableOfSortedItems(projectHandler, itemsAsStr);
    }

    public String genTableOfSortedItemsFromDB(DBService dbService, ProjectHandler projectHandler,User user) throws IOException, SQLException {
        List<List<String>> itemsAsStr = new DBWorker(user,dbService).getAllFromDb(SortingMenu.ITEM_ID.getDbColumn(),user,projectHandler.getItemHandler(), dbService.getConnection());
        return genTableOfSortedItems(projectHandler, itemsAsStr);
    }

    public String genTableOfSortedTypeOfItemsFromDB(DBService dbService, ProjectHandler projectHandler,User user) throws IOException, SQLException {
        List<List<String>> itemsAsStr = new DBWorker(user,dbService).getAnyTypeFromDB(SortingMenu.ITEM_ID.getDbColumn(),user,projectHandler.getItemHandler(), dbService.getConnection());
        return genTableOfSortedItems(projectHandler, itemsAsStr);
    }

    public List<List<String>> getItemsAsStringListSortedByComparator(ItemHandler itemHandler, Librarian librarian, String comparator) throws IOException {
        SortingMenu sortingParam = SortingMenu.getByOption(comparator);
        List<Item> items = librarian.initSortingItemsByComparator(sortingParam, itemHandler);
        return itemHandler.anyItemsToString(items);
    }


}
