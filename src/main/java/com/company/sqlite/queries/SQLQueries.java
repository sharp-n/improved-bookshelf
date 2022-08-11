package com.company.sqlite.queries;

import com.company.User;
import com.company.items.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@AllArgsConstructor
@NoArgsConstructor
public abstract class SQLQueries<T extends Item> {

    Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void insertItemToTable(Item item, User user){
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

    public void updateBorrowedItem(Item item, boolean borrow, User user){
        try{
            String typeOfItem = item.getClass().getSimpleName().toLowerCase();
            String query = "UPDATE " + user.userName + "_" + typeOfItem + "s set borrowed = " + borrow + " WHERE title = '" + item.getTitle() + "';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void deleteItem(Item item, User user) {
        try{
            String typeOfClass = item.getClass().getSimpleName().toLowerCase();
            String query = "DELETE from " + user.userName + "_" + typeOfClass + "s WHERE title = '" + item.getTitle() + "';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public ResultSet showSortedItems(String typeOfItem, String comparator, User user){
        try{
            typeOfItem = typeOfItem.toLowerCase();
            String query = "SELECT * FROM " + user.userName + "_" + typeOfItem + "s" + " ORDER BY " + comparator + " ASC;";
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    public ResultSet showSortedItems(String comparator, User user){
        try{
            String userName = user.userName;
            String query = "SELECT * FROM " + userName + "_books FULL OUTER JOIN " +
                    "(SELECT * FROM " + userName + "_comics FULL OUTER JOIN " +
                        "(SELECT * FROM" + userName + "_journals FULL OUTER JOIN " + userName + "_newspapers)) ORDER BY " + userName + "_books." + comparator + " ASC;";
            String query1 = "SELECT * FROM " + "yana" + "_journals;";
            Statement statement = connection.createStatement();
            return statement.executeQuery(query1);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    public void createTable(String typeOfItem, User user) {
        try{
            typeOfItem = typeOfItem.toLowerCase();
            String query = "CREATE TABLE IF NOT EXISTS " + user.userName + "_" + typeOfItem + "s" + " (" +
                    "item_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
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
