package com.company;

import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ItemsConvertor {

    public List<List<String>> itemsToString(List<Journal> journals) {
        List<List<String>> journalsAsStringList = new ArrayList<>();
        for (Journal journal: journals) {
            ItemConvertor journalConvertor = new JournalConvertor(journal);
            journalsAsStringList.add(journalConvertor.itemToString());
        }
        return journalsAsStringList;
    }

    public List<List<String>> booksToString(List<Book> books) {
        List<List<String>> booksAsStringList = new ArrayList<>();
        for (Book book: books) {
            ItemConvertor itemConvertor = new BookConvertor(book);
            booksAsStringList.add(itemConvertor.itemToString());
        }
        return booksAsStringList;
    }

    public List<List<String>> containersToString(List<Container<? extends Item>> containers){
        List<List<String>> containersAsStringList = new ArrayList<>();
        for (Container<? extends Item> container: containers){
            if (container.typeOfClass.equals("Book")){
                ItemConvertor bookConvertor = new BookConvertor(((Book)container.item));
                containersAsStringList.add(bookConvertor.itemToString());
            } else if (container.typeOfClass.equals("Journal")){
                ItemConvertor journalConvertor = new JournalConvertor(((Journal)container.item));
                containersAsStringList.add(journalConvertor.itemToString());
            }
        }
        return containersAsStringList;
    }

}
