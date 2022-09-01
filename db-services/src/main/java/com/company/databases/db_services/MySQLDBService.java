package com.company.databases.db_services;

import com.company.databases.queries.MySQLQueries;
import com.company.databases.queries.SQLDefaultQueries;
import com.company.databases.queries.SQLQueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDBService extends DBService {

    Connection connection;

    private String user = "sharp";
    private String password = "1";

    private int port = 3306;

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    public void open(String dbName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Port for MySQL" + port);
            connection = DriverManager.getConnection("jdbc:mysql://mysql:" + port + "/" + dbName, user, password);
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
