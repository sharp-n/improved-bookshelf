package com.company.sqlite;


import com.company.User;
import com.company.handlers.DBWorker;
import com.company.handlers.item_handlers.JournalHandler;
import com.company.items.Journal;
import com.company.items.Newspaper;
import com.company.sqlite.queries.SQLDefaultQueries;
import com.company.sqlite.queries.SQLQueries;

import java.sql.SQLException;
import java.util.List;

public class SQLiteTest {

    public static void main(String [] args) throws SQLException {
        DBService dbService = new DBService();
        dbService.open();

        SQLQueries sqlQueries = new SQLDefaultQueries(dbService.connection);

        sqlQueries.createUsersTable();
        sqlQueries.createItemsTable();

        //sqlQueries.addUserToTable(new User("sof"));
        //sqlQueries.addUserToTable(new User("yana"));
        //sqlQueries.insertItemToTable(new Newspaper(1,"srbddxb title",45),new User("yana"));
        //sqlQueries.insertItemToTable(new Newspaper(1,"rfyukhmg",465),new User("yana"));
        //sqlQueries.insertItemToTable(new Newspaper(1,"sw4yretbh",254),new User("yana"));
        //sqlQueries.insertItemToTable(new Newspaper(1,"s;lfdgbk",982),new User("yana"));

        List<List<String>> items = new DBWorker().getAnyTypeFromDB("journal","title",new User("yana"),new JournalHandler(), dbService.connection);

        items.forEach(o->System.out.println(o));

        //sqlQueries.deleteItem(new Journal(1,"another one title",78), new User("sof"));



        dbService.close();
    }
}
