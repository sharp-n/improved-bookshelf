package com.company.sqlite.queries;

import com.company.items.Item;

import java.sql.Connection;

public class SQLDefaultQueries extends SQLQueries<Item>{
    public SQLDefaultQueries(Connection connection) {
        super(connection);
    }
}
