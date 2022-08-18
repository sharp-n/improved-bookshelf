package com.company.sqlite;

import com.company.sqlite.queries.SQLDefaultQueries;
import com.company.sqlite.queries.SQLQueries;

public class MySQLTest {

    public static void main(String [] args){
        DBService dbService = new DBService();
        dbService.open();

        SQLQueries sqlQueries = new SQLDefaultQueries(dbService.connection);
        sqlQueries.createDatabase();
    }
}
