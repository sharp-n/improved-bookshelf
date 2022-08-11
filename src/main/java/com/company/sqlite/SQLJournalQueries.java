package com.company.sqlite;

import com.company.items.Journal;

import java.sql.Connection;

public class SQLJournalQueries extends SQLQueries<Journal> {

    public SQLJournalQueries(Connection connection) {
        super(connection);
    }

}
