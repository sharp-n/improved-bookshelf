package com.company.databases.queries;

import com.company.User;
import com.company.Book;
import com.company.Item;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class SQLBooksQueries  extends SQLQueries<Book> {

    private static final Logger log
            = Logger.getLogger(SQLBooksQueries.class);

    public SQLBooksQueries(Connection connection) {
        super(connection);
    }

    @Override
    public boolean insertItemToTable(Item item, User user){
        try{
            Book book = (Book) item;
            int borrowedSQL ;
            if(book.isBorrowed()){
                borrowedSQL = 1;
            } else borrowedSQL = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.M.y");
            String publishingDateStr = sdf.format(book.getDate().getTime());
            String query = "INSERT INTO items (user_id,title, author, publishing_date, pages, borrowed, type_of_item)" +
                    " VALUES ((SELECT user_id FROM users WHERE username = '" + user.userName + "')," +
                    "'" + book.getTitle() +"','" + book.getAuthor() + "'," +
                    "'" + publishingDateStr + "',"    + book.getPages() + "," +
                    "'" + borrowedSQL + "','book');";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(query);
            return result != 0;
        } catch(SQLException sqlException){
            log.error(sqlException.getMessage() + " : " + SQLBooksQueries.class.getSimpleName() + " : insertItemToTable()");
            return false;
        }
    }

}
