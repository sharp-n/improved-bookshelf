package com.company.handlers;

import com.company.User;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.items.Item;
import com.company.sqlite.queries.SQLQueries;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBWorker extends Librarian{



    @Override
    public boolean addItem(Item item) throws IOException {

        return super.addItem(item);
    }

    @Override
    public boolean deleteItem(Integer itemID, boolean forBorrow) throws IOException {
        return super.deleteItem(itemID, forBorrow);
    }

    @Override
    public boolean borrowItem(Integer itemID, boolean borrow) throws IOException {
        return super.borrowItem(itemID, borrow);
    }

    @Override
    public void initSorting(ItemHandler<? extends Item> itemHandler) throws IOException {
        super.initSorting(itemHandler);
    }


    void addToDB(Item item, User user, ItemHandler<Item> itemHandler, Connection connection){
        SQLQueries<? extends Item> sqlQueries = ItemHandlerProvider.getSQLQueryClassByHandler(itemHandler, connection);
        sqlQueries.createItemsTable();
        sqlQueries.insertItemToTable(item,user);
    }

    void deleteFromDB(Item item, User user, ItemHandler<Item> itemHandler, Connection connection){
        SQLQueries<? extends Item> sqlQueries = ItemHandlerProvider.getSQLQueryClassByHandler(itemHandler, connection);
        sqlQueries.deleteItem(item,user);
    }

    void borrowFromDB(Item item, User user, ItemHandler<Item> itemHandler, Connection connection){
        SQLQueries<? extends Item> sqlQueries = ItemHandlerProvider.getSQLQueryClassByHandler(itemHandler, connection);
        sqlQueries.updateBorrowedItem(item,true,user);
    }

    void returnToDb(Item item, User user, ItemHandler<? extends Item> itemHandler, Connection connection){
        SQLQueries<? extends Item> sqlQueries = ItemHandlerProvider.getSQLQueryClassByHandler(itemHandler, connection);
        sqlQueries.updateBorrowedItem(item,false,user);
    }

    public List<List<String>> getAllFromDb(String comparator, User user, ItemHandler<? extends Item> itemHandler, Connection connection) throws SQLException {
        SQLQueries<? extends Item> sqlQueries = ItemHandlerProvider.getSQLQueryClassByHandler(itemHandler, connection);
        ResultSet resultSet = sqlQueries.showSortedItems(comparator, user);
        List<List<String>> itemsStr = (itemHandler.getItemsAsStringListFromResultSet(resultSet));
        return itemsStr;
    }

    public List<List<String>> getAnyTypeFromDB(String typeOfItem, String comparator, User user, ItemHandler<? extends Item> itemHandler, Connection connection) throws SQLException {
        SQLQueries<? extends Item> sqlQueries = ItemHandlerProvider.getSQLQueryClassByHandler(itemHandler, connection);
        ResultSet resultSet = sqlQueries.showSortedItems(typeOfItem,comparator,user);
        List<List<String>> itemsStr = (itemHandler.getItemsAsStringListFromResultSet(resultSet));
        return itemsStr;
    }



}
