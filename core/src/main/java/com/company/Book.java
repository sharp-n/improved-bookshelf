package com.company;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.GregorianCalendar;

@NoArgsConstructor
@Data
public class Book extends Item {

    private String author;
    private GregorianCalendar publishingDate;

    public Book(int bookID, String title, String author, GregorianCalendar publishingDate, int pages) {
        super(bookID,title,pages);
        this.author = author;
        this.publishingDate = publishingDate;
    }

    public Book(int bookID, String title, String author, GregorianCalendar publishingDate, int pages, boolean borrowed) {
        super(bookID,title,pages,borrowed);
        this.author = author;
        this.publishingDate = publishingDate;
    }

}
