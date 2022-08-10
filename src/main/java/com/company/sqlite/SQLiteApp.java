package com.company.sqlite;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteApp {


    public static void main(String[] args){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:");
        } catch (SQLException | ClassNotFoundException cnfe){
            System.out.println(cnfe.getMessage());
        }
    }
}
