package com.company.sqlite.queries;

import com.company.User;
import com.company.items.Item;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@AllArgsConstructor
public class SQLQueries<T extends Item> {

    Connection connection;

    public void insertItemToTable(T item, User user){
        try{
            int borrowedSQL ;
            if(item.isBorrowed()){
                borrowedSQL = 1;
            } else borrowedSQL = 0;
            String typeOfItem = item.getClass().getSimpleName().toLowerCase();
            String query = "INSERT INTO " + user.userName + "_"+ typeOfItem + "s (title, pages, borrowed)" +
                    " VALUES ('" + item.getTitle() +"'," + item.getPages() + ",'" + borrowedSQL + "');";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void updateBorrowedItem(T item, boolean borrow, User user){
        try{
            String typeOfItem = item.getClass().getSimpleName().toLowerCase();
            String query = "UPDATE " + user.userName + "_" + typeOfItem + "s set borrowed = " + borrow + " WHERE title = '" + item.getTitle() + "';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void deleteItem(T item, User user) {
        try{
            String typeOfClass = item.getClass().getSimpleName().toLowerCase();
            String query = "DELETE from " + user.userName + "_" + typeOfClass + "s WHERE title = '" + item.getTitle() + "';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void showSortedItems(String typeOfItem, String comparator, User user){
        try{
            String query = "SELECT * FROM " + user.userName + "_" + typeOfItem + "s" + " ORDER BY " + comparator + " ASC;";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void showSortedItems(String comparator, User user){
        try{
            String userName = user.userName;
            String query = "SELECT * FROM " + userName + "_books FULL OUTER JOIN " +
                    "(SELECT * FROM " + userName + "_comics FULL OUTER JOIN " +
                        "(SELECT * FROM" + userName + "_journals FULL OUTER JOIN " + userName + "_newspapers)) ORDER BY " + comparator + " ASC;";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void createTable(String typeOfItem, User user) {
        try{
            String query = "CREATE TABLE IF NOT EXISTS " + user.userName + "_" + typeOfItem + "s" + " (" +
                    typeOfItem + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "title VARCHAR(50) NOT NULL, " +
                    "pages   VARCHAR(50) NOT NULL CHECK ( pages>0 ), " +
                    "borrowed INTEGER NOT NULL CHECK ( borrowed==0 | borrowed==1 )" +
                    ")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
