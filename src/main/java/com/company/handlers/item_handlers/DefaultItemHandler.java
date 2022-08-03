package com.company.handlers.item_handlers;

import com.company.items.Item;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class DefaultItemHandler extends ItemHandler<Item> {

    @Override
    public List<Item> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator) {
        return null;
    }

    @Override
    public List<List<String>> anyItemsToString(List<Item> items) {
        return null;
    }
}
