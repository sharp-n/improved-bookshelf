package com.company;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Type;

@AllArgsConstructor

public class Container<T> {

    T item;
    String typeOfClass;

    public Container(T item) {
        this.item = item;
        if(item instanceof Book) this.typeOfClass = "Book";
        if(item instanceof Journal) this.typeOfClass = "Journal";
    }

    public Book getBook() {
        System.out.println(item);
        //xBook b = (Book) item;
        return (Book) item;
    }

    public Journal getJournal() {
        return (Journal) item;
    }
}
