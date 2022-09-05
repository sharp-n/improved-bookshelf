package com.company.databases.queries;

import com.company.Item;

import java.sql.Connection;

public class SQLDefaultQueries extends SQLQueries<Item>{
    public SQLDefaultQueries(Connection connection) {
        super(connection);
    }
}
