package com.company;

import com.company.items.Item;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Container<T extends Item> {

    T item;
    String typeOfClass;

    public Container(T item) {
        this.item = item;
        this.typeOfClass = item.getClass().getSimpleName();
    }

}
