package com.company.databases;

import com.company.databases.db_services.DBService;
import com.company.databases.db_services.SQLiteDBService;
import com.company.databases.queries.SQLDefaultQueries;
import com.company.databases.queries.SQLQueries;

import java.sql.SQLException;

public class SQLiteTest {

    public static void main(String [] args) throws SQLException {
        DBService dbService = new SQLiteDBService();
        dbService.open("bookshelf.db");

        SQLQueries sqlQueries = new SQLDefaultQueries(dbService.getConnection());

        sqlQueries.createUsersTable();
        sqlQueries.createItemsTable();

        //sqlQueries.addUserToTable(new User("sof"));
        //sqlQueries.addUserToTable(new User("yana"));
        //sqlQueries.insertItemToTable(new Newspaper(1,"srbddxb title",45),new User("yana"));
        //sqlQueries.insertItemToTable(new Newspaper(1,"rfyaesfgukhmg",465),new User("sof"));
        //sqlQueries.insertItemToTable(new Newspaper(1,"sw4yretbh",254),new User("yana"));
        //sqlQueries.insertItemToTable(new Journal(1,"AKOFDS",982),new User("yana"));

        //sqlQueries.updateBorrowedItem(new Newspaper(1,"rfyaesfgukhmg",465),false, new User("sof"));

        //sqlQueries.deleteItem(new Newspaper(1,"rfyaesfgukhmg",465),new User("sof"));

        //List<List<String>> items = new DBWorker().getAllFromDb("title",new User("yana"),new JournalHandler(), dbService.connection);

        //items.forEach(o->System.out.println(o));

        //sqlQueries.deleteItem(new Journal(1,"another one title",78), new User("sof"));



        dbService.close();
    }
}
