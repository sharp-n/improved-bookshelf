package com.company;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Container<T> {

    T item;
    String typeOfClass;

    public Container(T item) {
        this.item = item;
        if(item instanceof Book) {this.typeOfClass = "Book";}
        if(item instanceof Journal) {this.typeOfClass = "Journal";}
    }

}
