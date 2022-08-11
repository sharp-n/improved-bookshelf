package com.company.sqlite;

import com.company.work_with_files.FilesWorker;
import com.company.work_with_files.OneFileWorker;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {

    Connection connection;
    void createDBIfNotExist(){
        FilesWorker filesWorker = new OneFileWorker(System.getProperty("user.home"),"yana");
        filesWorker.createFileIfNotExists(Paths.get("bookshelf.db"));
    }

    void open(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:bookshelf.db");
            System.out.println("Connection opened");
        } catch (SQLException | ClassNotFoundException cnfe){
            System.out.println(cnfe.getMessage());
        }
    }

    void close(){
        try {
            connection.close();
            System.out.println("Connection closed");
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

}
