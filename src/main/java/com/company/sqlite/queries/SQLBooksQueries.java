package com.company.sqlite.queries;

import com.company.User;
import com.company.items.Book;
import com.company.sqlite.queries.SQLQueries;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class SQLBooksQueries  extends SQLQueries<Book> {

    public SQLBooksQueries(Connection connection) {
        super(connection);
    }

    @Override
    public void insertItemToTable(Book item, User user){
        try{
            int borrowedSQL ;
            if(item.isBorrowed()){
                borrowedSQL = 1;
            } else borrowedSQL = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.M.y");
            String publishingDateStr = sdf.format(item.getPublishingDate().getTime());
            String query = "INSERT INTO " + user.userName + "_books (title, author, publishing_date, pages, borrowed)" +
                    " VALUES ('" + item.getTitle() +"','" + item.getAuthor() + "','" + publishingDateStr + "',"    + item.getPages() + ",'" + borrowedSQL + "');";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    @Override
    public void createTable(String typeOfItem, User user) {
        try{
            String query = "CREATE TABLE IF NOT EXISTS " + user.userName + "_" + typeOfItem + "s" + " (" +
                    typeOfItem + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "title VARCHAR(50) NOT NULL, " +
                    "author VARCHAR(50) NOT NULL, " +
                    "publishing_date varchar(20) NOT NULL, " +
                    "pages   VARCHAR(50) NOT NULL CHECK ( pages>0 ), " +
                    "borrowed INTEGER NOT NULL CHECK ( borrowed==0 | borrowed==1 )" +
                    ")";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
