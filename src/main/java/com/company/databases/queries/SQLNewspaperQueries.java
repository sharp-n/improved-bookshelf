package com.company.databases.queries;


import com.company.items.Newspaper;

import java.sql.Connection;

public class SQLNewspaperQueries extends SQLQueries<Newspaper> {
    public SQLNewspaperQueries(Connection connection) {
        super(connection);
    }
}
