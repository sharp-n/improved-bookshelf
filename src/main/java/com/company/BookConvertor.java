package com.company;

import com.company.items.Book;
import com.company.items.Item;
import jdk.tools.jlink.internal.plugins.SystemModulesPlugin;
import sun.java2d.pipe.SpanShapeRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BookConvertor extends ItemConvertor{

    Book book;

    public BookConvertor(Book book) {
        super(book);
        this.book = book;
    }

    public String authorToString(){
        return book.getAuthor();
    }

    public String publishingDateToString(){
        SimpleDateFormat df = new SimpleDateFormat("dd.M.y");
        return df.format(book.getPublishingDate().getTime());
    }

    public List<String> itemToString(){
        List<String> bookAsList = new ArrayList<>();
        bookAsList.add(idToString());
        bookAsList.add(titleToString());
        bookAsList.add(authorToString());
        bookAsList.add(publishingDateToString());
        bookAsList.add(pagesToString());
        bookAsList.add(borrowedToString());
        return bookAsList;
    }

}
