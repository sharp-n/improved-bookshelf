package com.company.handlers;

import com.company.ComparatorsForSorting;
import com.company.User;
import com.company.enums.SortingMenu;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.items.Item;
import com.company.sqlite.queries.SQLDefaultQueries;
import com.company.sqlite.queries.SQLQueries;
import com.company.table.TableUtil;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class DBWorker extends Librarian{

    // todo add usage of database to servlets

    User user;
    Connection connection;
    public DBWorker(User user, Connection connection, PrintWriter out) {
        super.out = out;
        this.user = user;
        this.connection = connection;
    }

    @Override
    public boolean addItem(ItemHandler<? extends Item> itemHandler, List<String> itemOptions) throws IOException {
        Item item = itemHandler.createItem(itemOptions);
        return addToDB(item,user,itemHandler,connection);
    }

    @Override
    public boolean addItem(Item item){ // todo check method
        ItemHandler<Item> itemHandler = ItemHandlerProvider.getHandlerByClass(
                ItemHandlerProvider.getClassBySimpleNameOfClass(item.getClass().getSimpleName()));
        return addToDB(item,user,itemHandler,connection);
    }

    @Override
    public boolean deleteItem(Integer itemID, boolean forBorrow) { // todo check method
        return deleteFromDB(itemID,user,connection);
    }

    @Override
    public boolean borrowItem(Integer itemID, boolean borrow){ // todo check method
        if(borrow){
            return borrowFromDB(itemID,user,connection);
        } else {
            return returnToDb(itemID,user,connection);
        }
    }

    @Override
    public void initSorting(ItemHandler<? extends Item> itemHandler) throws IOException { // todo check method
        Integer usersChoice = itemHandler.userInput.getSortingVar(itemHandler.genSortingMenuText());
        if (usersChoice != null) {
            SortingMenu sortingParameter = SortingMenu.getByIndex(usersChoice);
            List<List<String>> sortedItemsByComparator = getAnyTypeFromDB(sortingParameter.getDbColumn(),user,itemHandler,connection);
            if(!sortedItemsByComparator.isEmpty()){
                List<String> options = itemHandler.getColumnTitles();
                TableUtil tableUtil = new TableUtil(options, sortedItemsByComparator, out);
                tableUtil.printTable();
            }
        } else{
            itemHandler.userInput.printDefaultMessage();
        }
    }

    boolean addToDB(Item item, User user, ItemHandler<? extends Item> itemHandler, Connection connection){
        SQLQueries<? extends Item> sqlQueries = ItemHandlerProvider.getSQLQueryClassByHandler(itemHandler, connection);
        sqlQueries.createItemsTable();
        return sqlQueries.insertItemToTable(item,user);
    }

    boolean deleteFromDB(int itemID, User user, Connection connection){
        return new SQLDefaultQueries(connection).deleteItem(itemID,user);

    }

    boolean borrowFromDB(int itemID, User user, Connection connection){
        return new SQLDefaultQueries(connection).updateBorrowedItem(itemID,true,user);

    }

    boolean returnToDb(int itemID, User user, Connection connection){
        return new SQLDefaultQueries(connection).updateBorrowedItem(itemID,false,user);
    }

    public List<List<String>> getAllFromDb(String comparator, User user, ItemHandler<? extends Item> itemHandler, Connection connection) throws SQLException {
        SQLQueries<? extends Item> sqlQueries = ItemHandlerProvider.getSQLQueryClassByHandler(itemHandler, connection);
        ResultSet resultSet = sqlQueries.showSortedItems(comparator, user);
        return (itemHandler.getItemsAsStringListFromResultSet(resultSet));
    }

    public List<List<String>> getAnyTypeFromDB(String comparator, User user, ItemHandler<? extends Item> itemHandler, Connection connection) {
        try {
            String typeOfItem = ItemHandlerProvider.getClassByHandler(itemHandler).getSimpleName().toLowerCase();
            SQLQueries<? extends Item> sqlQueries = ItemHandlerProvider.getSQLQueryClassByHandler(itemHandler, connection);
            ResultSet resultSet = sqlQueries.showSortedItems(typeOfItem, comparator, user);
            return (itemHandler.getItemsAsStringListFromResultSet(resultSet));
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    public List<? extends Item> initSortingItemsByComparator(SortingMenu sortingParameter, ItemHandler<? extends Item> itemHandler) throws IOException {
        List<List<String>> sortedItemsStr = getAnyTypeFromDB(sortingParameter.getDbColumn(),user,itemHandler,connection);
        List<Item> items = new ArrayList<>();
        for (List<String> itemStr : sortedItemsStr){
            Item item = itemHandler.createItem(itemStr);
            items.add(item);
        }
        return items;
    }

}
