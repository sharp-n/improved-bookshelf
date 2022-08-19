package com.company.databases.db_services;

import com.company.databases.db_services.DBService;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDBService extends DBService {

    Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    public void open() {

        try {
            Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection("jdbc:sqlite:bookshelf.db");
            System.out.println("Connection opened");

        } catch (SQLException | ClassNotFoundException sqlException){
            sqlException.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
