package com.company.sqlite;

import com.company.User;
import com.company.handlers.Librarian;
import com.company.handlers.item_handlers.JournalHandler;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.sqlite.queries.SQLDefaultQueries;
import com.company.sqlite.queries.SQLQueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SQLiteApp {

    Connection connection; // todo add database usage to main app

    public static void main(String[] args) throws SQLException {
        SQLiteApp sqLiteApp = new SQLiteApp();
        DBService dbService = new DBService();
        dbService.open();
        //dbService.createDBIfNotExist();

        SQLQueries<Item> sqlQueries = new SQLDefaultQueries(dbService.connection);

        List<List<String>> items = new Librarian().getAllFromDb("title",new User("yana"),new JournalHandler(), dbService.connection);
        System.out.println(items);
        //sqlQueries.createTable("journal", new User("yana"));
        //sqlQueries.insertItemToTable(new Journal(1,"title",43), new User("yana"));
       dbService.close();
    }




}
