package com.company;

import com.company.User;
import com.company.databases.queries.SQLDefaultQueries;
import com.company.databases.queries.SQLQueries;


import java.sql.Connection;
import java.sql.SQLException;

public abstract class DBService {

//    private static final org.apache.log4j.Logger log
//            = org.apache.log4j.Logger.getLogger(DBService.class);

    Connection connection;

    public abstract Connection getConnection();

    public abstract void open(String dbName);

    public abstract void createTablesIfNotExist(Connection connection);

    public abstract void createUsersTableIfNotExists(Connection connection);

    public abstract void createItemsTableIfNotExists(Connection connection);

    public void createUser(User user, Connection connection) {
        SQLQueries sqlDefaultQueries = new SQLDefaultQueries(connection);

        String username = sqlDefaultQueries.getUser(user);
        if (username == null) {
            sqlDefaultQueries.addUserToTable(user);
        }
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException sqlException){
//            log.error(sqlException.getMessage() + " : " + DBService.class.getSimpleName() + " : close()");
            System.out.println(sqlException.getMessage());
        }
    }

}
