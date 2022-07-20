package com.company.convertors;

import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.items.Newspaper;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ItemsConvertor {

    public List<List<String>> journalsToString(List<Journal> journals) {
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

    public List<List<String>> itemsToString(List<? extends Item> items){
        List<List<String>> containersAsStringList = new ArrayList<>();
        for (Item item: items){
            if (item instanceof Book){
                ItemConvertor bookConvertor = new BookConvertor(((Book)item));
                containersAsStringList.add(bookConvertor.itemToString());
            } else if (item instanceof Journal){
                ItemConvertor journalConvertor = new JournalConvertor(((Journal)item));
                containersAsStringList.add(journalConvertor.itemToString());
            }
            else if (item instanceof Newspaper){
                ItemConvertor newspaperConvertor = new NewspaperConvertor((Newspaper)item);
                containersAsStringList.add(newspaperConvertor.itemToString());
            }
        }
        return containersAsStringList;
    }

}
