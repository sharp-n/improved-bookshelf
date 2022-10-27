package com.company;

import com.company.databases.queries.SQLDefaultQueries;
import com.company.databases.queries.SQLQueries;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDBService extends DBService {

//    private static final org.apache.log4j.Logger log
//            = org.apache.log4j.Logger.getLogger(SQLiteDBService.class);


    Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    public void open(String dbName) {

        try {
            Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);

        } catch (SQLException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException exception){
//            log.error(exception.getMessage() + " : " + SQLiteDBService.class.getSimpleName() + " : open()");
        }
    }

    public void createTablesIfNotExist(Connection connection){
        createUsersTableIfNotExists(connection);
        createItemsTableIfNotExists(connection);
    }

    public void createUsersTableIfNotExists(Connection connection){
        SQLQueries sqlQueries = new SQLDefaultQueries(connection);
        sqlQueries.createUsersTable();
    }

    public void createItemsTableIfNotExists(Connection connection){
        SQLQueries sqlQueries = new SQLDefaultQueries(connection);
        sqlQueries.createItemsTable();
    }

}
