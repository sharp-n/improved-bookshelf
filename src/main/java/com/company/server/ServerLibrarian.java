package com.company.server;

import com.company.Book;
import com.company.Item;
import com.company.Journal;
import com.company.Librarian;

import java.io.IOException;

public class ServerLibrarian extends Librarian {

    @Override
    public void addItem(Item item) throws IOException {
        if (item instanceof Book){
            checkIDForExistence(item.getItemID(),"Book");
        } else if (item instanceof Journal){
            checkIDForExistence(item.getItemID(),"Journal");
        }
        super.addItem(item);
    }


}
