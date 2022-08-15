package com.company.sqlite.queries;

import com.company.User;
import com.company.items.Comics;
import com.company.items.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLComicsQueries extends SQLQueries<Comics> {

    public SQLComicsQueries(Connection connection) {
        super(connection);
    }

    @Override
    public void insertItemToTable(Item item, User user){
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
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }


}
