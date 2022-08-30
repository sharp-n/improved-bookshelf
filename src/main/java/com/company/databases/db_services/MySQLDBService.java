package com.company.databases.db_services;

import com.company.databases.queries.MySQLQueries;
import com.company.databases.queries.SQLDefaultQueries;
import com.company.databases.queries.SQLQueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDBService extends DBService {

    Connection connection;

    private static final String user = "sharp";
    private static final String password = "1";

    @Override
    public Connection getConnection() {
        return connection;
    }

    public void open() {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookshelf", user, password);

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void createTablesIfNotExist(Connection connection){
        createUsersTableIfNotExists(connection);
        createItemsTableIfNotExists(connection);
    }

    public void createUsersTableIfNotExists(Connection connection){
        SQLQueries sqlQueries = new MySQLQueries(connection);
        sqlQueries.createUsersTable();
    }

    public void createItemsTableIfNotExists(Connection connection){
        SQLQueries sqlQueries = new MySQLQueries(connection);
        sqlQueries.createItemsTable();
    }

}
