package com.company.databases;

import com.company.User;
import com.company.databases.db_services.DBService;
import com.company.databases.db_services.MySQLDBService;
import com.company.databases.queries.MySQLQueries;
import com.company.databases.queries.SQLDefaultQueries;
import com.company.databases.queries.SQLQueries;

public class MySQLTest {

    public static void main(String [] args){
        DBService dbService = new MySQLDBService();
        dbService.open();
        SQLQueries sqlQueries = new MySQLQueries(dbService.getConnection());
        //sqlQueries.createUsersTable();
        //sqlQueries.createItemsTable();
        dbService.createUser(new User("sofiya"),dbService.getConnection());
        //dbService.close();
    }
}
