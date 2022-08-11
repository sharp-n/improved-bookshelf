package com.company.sqlite;

import com.company.User;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.sqlite.queries.SQLDefaultQueries;
import com.company.sqlite.queries.SQLQueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteApp {

    Connection connection;

    public static void main(String[] args){
        SQLiteApp sqLiteApp = new SQLiteApp();
        DBService dbService = new DBService();
        sqLiteApp.open();
        dbService.createDBIfNotExist();

        SQLQueries<Item> sqlQueries = new SQLDefaultQueries(sqLiteApp.connection);
        sqlQueries.createTable("journal", new User("yana"));
        sqlQueries.insertItemToTable(new Journal(1,"title",43), new User("yana"));
        sqLiteApp.close();
    }

    void open(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:bookshelf.db");
            System.out.println("Connection opened");
        } catch (SQLException | ClassNotFoundException cnfe){
            System.out.println(cnfe.getMessage());
        }
    }

    void close(){
        try {
            connection.close();
            System.out.println("Connection closed");
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }


}
