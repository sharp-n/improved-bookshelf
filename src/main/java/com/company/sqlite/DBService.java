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

    private static final String user = "root";
    private static final String password = "";

    public Connection getConnection() {
        return connection;
    }

    void createDBIfNotExist(){
        FilesWorker filesWorker = new OneFileWorker(System.getProperty("user.home"),"yana");
        filesWorker.createFileIfNotExists(Paths.get("bookshelf.db"));
    }

    public void open() {

        try {
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookshelf", user, password);
//            connection = DriverManager.getConnection("jdbc:sqlite:bookshelf.db");
            System.out.println("Connection opened");

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
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
