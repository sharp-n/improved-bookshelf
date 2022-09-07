package com.company.databases.queries;

import com.company.Journal;

import java.sql.Connection;

public class SQLJournalQueries extends SQLQueries<Journal> {

    public SQLJournalQueries(Connection connection) {
        super(connection);
    }

}
