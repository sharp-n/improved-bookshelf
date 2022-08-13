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

    public void insertItemToTable(Item item, User user){ // checked
        try{
            int borrowedSQL ;
            if(item.isBorrowed()){ // for journals and newspapers
                borrowedSQL = 1;
            } else {
                borrowedSQL = 0;
            }
            String typeOfItem = item.getClass().getSimpleName().toLowerCase();
            String query = "INSERT INTO items (user_id,title, pages, borrowed,type_of_item)" +
                    " VALUES ((SELECT user_id FROM users WHERE username = '" + user.userName + "'),'" + item.getTitle() +"'," + item.getPages() + ",'" + borrowedSQL + "','" + typeOfItem + "');";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void addUserToTable(User user){ // checked
        try{
            String query = "INSERT INTO users (username) " +
                    "VALUES ('" + user.userName +"');";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void updateBorrowedItem(Item item, boolean borrow, User user){ // todo refactor query
        try{
            String query = "UPDATE items set borrowed = " + borrow +
                    " WHERE user_id = items.user_id " +
                    "AND title = '" + item.getTitle() + "';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void deleteItem(Item item, User user) { // todo refactor query
        try{
            String typeOfClass = item.getClass().getSimpleName().toLowerCase();
            String query = "DELETE from items " +
                    "WHERE user_id = items.user_id " +
                    "AND type_of_item = '" + typeOfClass + "' " +
                    "AND title = '" + item.getTitle() + "'" +";";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public ResultSet showSortedItems(String typeOfItem, String comparator, User user){ // fixme fix query - bad select
        try{
            typeOfItem = typeOfItem.toLowerCase();
            String query = "SELECT * FROM items " +
                    "CROSS JOIN users " +
                    "WHERE username = '" + user.userName + "'" +
                    "AND type_of_item = '" + typeOfItem + "' " +
                    "ORDER BY " + comparator + " ASC;";
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    public ResultSet showSortedItems(String comparator, User user){ // todo check for bugs
        try{
            String userName = user.userName;
            String query = "SELECT * FROM items " +
                    "CROSS JOIN users " +
                    "WHERE users.username = '" + userName + "'" +
                    "ORDER BY " + comparator + " ASC;";
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    public void createItemsTable() { // checked
        try{
            String query = "CREATE TABLE IF NOT EXISTS items (" +
                    "item_id   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "type_of_item VARCHAR(50) NOT NULL, " +
                    "title     VARCHAR(50) NOT NULL UNIQUE, " +
                    "author    VARCHAR(50),  " +
                    "publishing_date VARCHAR(50), " +
                    "publisher VARCHAR(50)," +
                    "pages     VARCHAR(50) NOT NULL CHECK ( pages>0 ), " +
                    "borrowed  INTEGER NOT NULL CHECK ( borrowed==0 | borrowed==1 )," +
                    "user_id   INTEGER NOT NULL," +
                    "FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE" +
                    ")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void createUsersTable() { // checked
        try{
            String query = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "username VARCHAR(20) NOT NULL UNIQUE " +
                    ")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
