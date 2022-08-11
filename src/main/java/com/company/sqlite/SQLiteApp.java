package com.company.sqlite;

import com.company.User;
import com.company.items.Journal;
import com.company.sqlite.queries.SQLQueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteApp {

    Connection connection;

    public static void main(String[] args){
        SQLiteApp sqLiteApp = new SQLiteApp();
        DBService dbService = new DBService();
        sqLiteApp.open();
        dbService.createDBIfNotExist();

        SQLQueries sqlQueries = new SQLQueries(sqLiteApp.connection);
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

    void createTable(){
        try {
            String query = "CREATE TABLE IF NOT EXISTS books(" +
                    "book_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "title   TEXT(50) NOT NULL , " +
                    "author  TEXT(50) NOT NULL , " +
                    "pages   TEXT(50) NOT NULL CHECK ( pages>0 ), " +
                    "day     INTEGER NOT NULL CHECK (day>0&day<32), " +
                    "month   INTEGER NOT NULL CHECK (month<12&month>0) , " +
                    "year    INTEGER NOT NULL CHECK (year<2022&year>0) " +
                    ")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    void insertBook(){
        try{
            String query = "INSERT INTO books (title, author, pages, day, month, YEAR)" +
                    " VALUES ('title','author',926,3,4,2022);";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch(SQLException sqlException){
            sqlException.printStackTrace();
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
