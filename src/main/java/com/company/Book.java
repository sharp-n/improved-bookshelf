package com.company;

import java.util.GregorianCalendar;

public class Book extends Item{

    private String author;
    private GregorianCalendar publishingDate;

    public Book(){
    }

    public Book(int bookID, String title, String author, GregorianCalendar publishingDate, int pages ) {
        super.itemID = bookID;
        super.title = title;
        this.author = author;
        this.publishingDate = publishingDate;
        super.pages = pages;
        super.borrowed = false;
    }

    public String getAuthor() {
        return author;
    }

    public GregorianCalendar getPublishingDate() {
        return publishingDate;
    }

}