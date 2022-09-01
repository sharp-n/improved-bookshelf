package com.company.databases.queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLQueries extends SQLQueries{

    public MySQLQueries(Connection connection) {
        super(connection);
    }

    @Override
    public void createItemsTable() {
        try{
            String query = "CREATE TABLE IF NOT EXISTS items (" +
                    "item_id   INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                    "type_of_item VARCHAR(50) NOT NULL, " +
                    "title     VARCHAR(50) NOT NULL UNIQUE, " +
                    "author    VARCHAR(50),  " +
                    "publishing_date VARCHAR(50), " +
                    "publisher VARCHAR(50)," +
                    "pages     VARCHAR(50) NOT NULL CHECK ( pages>0 ), " +
                    "borrowed  BOOL NOT NULL," +
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
                    "user_id  INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                    "username VARCHAR(20) NOT NULL UNIQUE " +
                    ")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}
