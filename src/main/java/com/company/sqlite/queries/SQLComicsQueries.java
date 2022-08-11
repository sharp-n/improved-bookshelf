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
            String query = "INSERT INTO " + user.userName + "_comics (title, publisher, pages, borrowed)" +
                    " VALUES ('" + comics.getTitle() +"','" + comics.getPublishing() + "'," + comics.getPages() + ",'" + borrowedSQL + "');";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    @Override
    public void createTable(String typeOfItem, User user) {
        try{
            String query = "CREATE TABLE IF NOT EXISTS " + user.userName + "_comics" + " (" +
                    typeOfItem + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "title VARCHAR(50) NOT NULL, " +
                    "publisher VARCHAR(50) NOT NULL," +
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
