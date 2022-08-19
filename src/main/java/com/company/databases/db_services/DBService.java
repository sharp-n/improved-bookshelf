package com.company.databases.db_services;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DBService {

    Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public abstract void open();

    public void close(){
        try {
            connection.close();
            System.out.println("Connection closed");
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

}
