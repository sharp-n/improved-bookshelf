package com.company.databases.db_services;

import com.company.databases.db_services.DBService;

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
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307/bookshelf", user, password);

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
