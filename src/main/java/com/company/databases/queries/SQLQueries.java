package com.company.databases.queries;

import com.company.User;
import com.company.enums.FilesMenu;
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

    public boolean insertItemToTable(Item item, User user){
        try{
            int borrowedSQL ;
            if(item.isBorrowed()){
                borrowedSQL = 1;
            } else {
                borrowedSQL = 0;
            }
            String typeOfItem = item.getClass().getSimpleName().toLowerCase();
            String query = "INSERT INTO items (user_id, title, pages, borrowed,type_of_item)" +
                    " VALUES ((SELECT user_id FROM users WHERE username = '" + user.userName + "')," +
                    "'" + item.getTitle() +"'," + item.getPages() + ",'" + borrowedSQL + "','" + typeOfItem + "');";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(query);
            return result != 0;
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return false;
        }
    }

    public boolean addUserToTable(User user){
        try{
            String query = "INSERT INTO users (username) " +
                    "VALUES ('" + user.userName +"');";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(query);
            return result != 0;
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return false;
        }
    }

//PDATE items,
//       (SELECT id FROM items
//        WHERE id IN
//            (SELECT id FROM items
//             WHERE retail / wholesale >= 1.3 AND quantity < 100))
//        AS discounted
//SET items.retail = items.retail * 0.9
//WHERE items.id = discounted.id;


    public boolean updateBorrowedItem(int itemID, boolean borrow, User user, String dbType){
        try{
            String query = "";
            if(dbType.equals(FilesMenu.DATABASE_MYSQL.getServletParameter())){
                query = "UPDATE items," +
                            "(SELECT user_id FROM users " +
                            "WHERE user_id IN " +
                                "(SELECT user_id FROM users " +
                                "WHERE username='" + user.userName + "'))" +
                        "AS i " +
                        "SET items.borrowed = " + borrow + " " +
                        "WHERE items.user_id=i.user_id;";
            } else if (dbType.equals(FilesMenu.DATABASE_SQLITE.getServletParameter())){
                query = "UPDATE items set borrowed = " + borrow +
                        " WHERE EXISTS" +
                        "(SELECT * " +
                        "FROM users " +
                        "CROSS JOIN items " +
                        "WHERE users.user_id = items.user_id " +
                        "AND username = '" + user.userName + "') " +
                        "AND item_id = " + itemID +"; ";
            }
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(query);
            return result != 0;
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(int itemId, User user) {
        try{
            String queryDel = "DELETE from items " +
                    "WHERE EXISTS" +
                    "(SELECT * " +
                    "FROM users " +
                    "CROSS JOIN items " +
                    "WHERE users.user_id = items.user_id " +
                    "AND username = '" + user.userName + "') " +
                    "AND item_id = " + itemId + ";";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(queryDel);
            return result != 0;
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return false;
        }
    }

    public ResultSet getItem(int itemID, User user){
        try {
            String query = "SELECT * " +
                    "FROM users " +
                    "CROSS JOIN items " +
                    "WHERE users.user_id = items.user_id " +
                    "AND username = '" + user.userName + "' " +
                    "AND item_id = " + itemID + ";";
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    public ResultSet showSortedItems(String typeOfItem, String comparator, User user){
        try{
            typeOfItem = typeOfItem.toLowerCase();
            String query = "SELECT * " +
                    "FROM items " +
                    "LEFT JOIN users " +
                    "ON users.user_id = items.user_id " +
                    "WHERE username = '" + user.userName + "' " +
                    "AND type_of_item = '" + typeOfItem + "' " +
                    "ORDER BY " + comparator + " ASC;";
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    public ResultSet showSortedItems(String comparator, User user){
        try{
            String query = "SELECT * " +
                    "FROM items " +
                    "LEFT JOIN users " +
                    "ON users.user_id = items.user_id " +
                    "WHERE username = '" + user.userName + "' " +
                    "ORDER BY " + comparator + " ASC;";
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    public void createItemsTable() {
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

    public void createUsersTable() {
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
    public void createDatabase(){
        try{
            String query = "CREATE DATABASE bookshelf;";
            Statement statement = connection.createStatement();
            statement.execute(query);
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
