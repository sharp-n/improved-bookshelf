package com.company.databases.db_services;

import com.company.User;
import com.company.databases.queries.SQLDefaultQueries;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public abstract class DBService {

    Connection connection;

    public abstract Connection getConnection();

    public abstract void open();

    public void createUser(User user, Connection connection) {
        SQLDefaultQueries sqlDefaultQueries = new SQLDefaultQueries(connection);
        String username = sqlDefaultQueries.getUser(user);
        System.out.println(username);
        if (username == null) {
            sqlDefaultQueries.addUserToTable(user);
        }
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }


}
