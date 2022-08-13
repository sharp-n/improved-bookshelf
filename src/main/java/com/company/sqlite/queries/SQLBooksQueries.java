package com.company.sqlite.queries;

import com.company.User;
import com.company.items.Book;
import com.company.items.Item;
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
    public void insertItemToTable(Item item, User user){
        try{
            Book book = (Book) item;
            int borrowedSQL ;
            if(book.isBorrowed()){
                borrowedSQL = 1;
            } else borrowedSQL = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.M.y");
            String publishingDateStr = sdf.format(book.getPublishingDate().getTime());
            String query = "INSERT INTO " + user.userName + "_books (title, author, publishing_date, pages, borrowed)" +
                    " VALUES ('" + book.getTitle() +"','" + book.getAuthor() + "','" + publishingDateStr + "',"    + book.getPages() + ",'" + borrowedSQL + "');";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

}
