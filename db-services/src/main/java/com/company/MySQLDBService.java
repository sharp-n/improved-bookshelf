package com.company;

import com.company.databases.queries.MySQLQueries;
import com.company.databases.queries.SQLQueries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDBService extends DBService {

//    private static final org.apache.log4j.Logger log
//            = org.apache.log4j.Logger.getLogger(SQLiteDBService.class);

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
            connection = DriverManager.getConnection("jdbc:mysql://mysql:" + port + "/" + dbName, user, password);
        } catch (SQLException | ClassNotFoundException exception) {
//            log.error(exception.getMessage() + " : " + MySQLDBService.class.getSimpleName() + " : open()");
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
