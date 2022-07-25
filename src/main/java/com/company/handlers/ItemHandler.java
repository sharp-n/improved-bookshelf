package com.company.handlers;

import com.company.items.Item;

import java.util.Comparator;
import java.util.List;

public interface ItemHandler<T extends Item> {

    // TODO add journal later

    List<T> getSortedItemsByComparator(List<T> items, Comparator<T> comparator);

    T createItem(List<String> options);


}
