package com.company.databases.queries;

import com.company.User;
import com.company.Comics;
import com.company.Item;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLComicsQueries extends SQLQueries<Comics> {

//    private static final Logger log
//            = Logger.getLogger(SQLComicsQueries.class);

    public SQLComicsQueries(Connection connection) {
        super(connection);
    }

    @Override
    public boolean insertItemToTable(Item item, User user){
        try{
            Comics comics = (Comics) item;
            int borrowedSQL ;
            if(comics.isBorrowed()){
                borrowedSQL = 1;
            } else borrowedSQL = 0;
            String query = "INSERT INTO items (user_id, title, pages, borrowed, publisher, type_of_item)" +
                    " VALUES ((SELECT user_id FROM users WHERE username = '" + user.userName + "')," +
                    "'" + comics.getTitle() +"',"   + comics.getPages() + "," +
                    "'" + borrowedSQL + "','" + comics.getPublishing() + "','comics');";
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(query);
            return result != 0;
        } catch(SQLException sqlException){
//            log.error(sqlException.getMessage() + " : " + SQLComicsQueries.class.getSimpleName() + " : insertItemToTable()");
            return false;
        }
    }

}
