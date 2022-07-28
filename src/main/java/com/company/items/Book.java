package com.company.items;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.GregorianCalendar;

@NoArgsConstructor
@Data
public class Book extends Item {

    private String author;
    private GregorianCalendar publishingDate;

    public Book(int bookID, String title, String author, GregorianCalendar publishingDate, int pages) {
        super.itemID = bookID;
        super.title = title;
        this.author = author;
        this.publishingDate = publishingDate;
        super.pages = pages;
        super.borrowed = false;
    }

}
