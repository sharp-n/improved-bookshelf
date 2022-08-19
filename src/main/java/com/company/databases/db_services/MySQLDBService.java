package com.company.databases.db_services;

import com.company.databases.db_services.DBService;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDBService extends DBService {

    private static final String user = "sharp";
    private static final String password = "1";

    public void open() {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307/bookshelf", user, password);
            System.out.println("Connection opened");

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
