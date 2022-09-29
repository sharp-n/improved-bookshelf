package com.company;

import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.DBWorker;
import com.company.handlers.Librarian;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.table.HtmlTableBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class WebAppService {

    public ProjectHandler genProjectHandlerFromParameters(ParametersForWeb param){
        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
        projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
        projectHandler.fileSwitch(FilesMenu.getByOption(param.typeOfWork), new User(param.name));
        return projectHandler;
    }

    public String genTableOfSortedItems(ProjectHandler projectHandler, List<List<String>> itemsAsStr) {
        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(projectHandler.getItemHandler().getColumnTitles(), itemsAsStr);
        return tableBuilder.generateTable();
    }

    public String genTableOfSortedItemsFromFiles(ParametersForWeb param, String sortingParam ) throws IOException {
        ProjectHandler projectHandler = genProjectHandlerFromParameters(param);
        List<List<String>> itemsAsStr = getItemsAsStringListSortedByComparator(projectHandler.getItemHandler(),projectHandler.getLibrarian(),sortingParam);
        return genTableOfSortedItems(projectHandler, itemsAsStr);
    }

    public String genTableOfSortedItemsFromDB(DBService dbService, ProjectHandler projectHandler,User user) throws SQLException {
        List<List<String>> itemsAsStr = new DBWorker(user,dbService).getAllFromDb(SortingMenu.ITEM_ID.getDbColumn(),user,projectHandler.getItemHandler(), dbService.getConnection());
        return genTableOfSortedItems(projectHandler, itemsAsStr);
    }

    public String genTableOfSortedTypeOfItemsFromDB(DBService dbService, ProjectHandler projectHandler,User user) {
        List<List<String>> itemsAsStr = new DBWorker(user,dbService).getAnyTypeFromDB(SortingMenu.ITEM_ID.getDbColumn(),user,projectHandler.getItemHandler(), dbService.getConnection());
        return genTableOfSortedItems(projectHandler, itemsAsStr);
    }

    public List<List<String>> getItemsAsStringListSortedByComparator(ItemHandler itemHandler, Librarian librarian, String comparator) throws IOException {
        SortingMenu sortingParam = SortingMenu.getByOption(comparator);
        List<Item> items = librarian.initSortingItemsByComparator(sortingParam, itemHandler);
        return itemHandler.anyItemsToString(items);
    }

    public String getTable(String comparator, ProjectHandler projectHandler, ParametersForWeb param) {
        try {
            String table = "";
            if (param.typeOfWork.equals(ParametersConstants.DATABASE_SQLite)
                    ||param.typeOfWork.equals(ParametersConstants.DATABASE_MYSQL)) {
                DBService dbService = DBServiceProvider.getDBServiceByOption(param.typeOfWork);
                dbService.open(DBServiceProvider.getDBNameByService(dbService));
                dbService.createTablesIfNotExist(dbService.getConnection());
                User user = new User(param.name);
                dbService.createUser(user,dbService.getConnection());
                table = genTableOfSortedTypeOfItemsFromDB(dbService, projectHandler, user);
            } else if (param.typeOfWork.equals(ParametersConstants.ONE_FILE)) {
                table = genTableOfSortedItemsFromFiles(param, comparator);
            }
            return table;
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

}
