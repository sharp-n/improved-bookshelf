package com.company.databases.db_services;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DBService {

    Connection connection;

    public abstract Connection getConnection();

    public abstract void open();

    public void close(){
        try {
            connection.close();
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

}
