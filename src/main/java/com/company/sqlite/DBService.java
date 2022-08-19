package com.company.sqlite;

import com.company.work_with_files.FilesWorker;
import com.company.work_with_files.OneFileWorker;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {

    Connection connection;

    private static final String user = "sharp";
    private static final String password = "1";

    public Connection getConnection() {
        return connection;
    }

    void createDBIfNotExist(){
        FilesWorker filesWorker = new OneFileWorker(System.getProperty("user.home"),"yana");
        filesWorker.createFileIfNotExists(Paths.get("bookshelf.db"));
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

    public void openMySQL() {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307/bookshelf", user, password);
            System.out.println("Connection opened");

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void close(){
        try {
            connection.close();
            System.out.println("Connection closed");
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

}
