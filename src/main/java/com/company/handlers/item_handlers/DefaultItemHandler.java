package com.company.handlers.item_handlers;

import com.company.items.Book;
import com.company.items.Item;
import com.company.table.TableUtil;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class DefaultItemHandler extends ItemHandler<Item> {

    @Override
    public List<Item> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator) {
        return items.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(List<String> options) {
        return null;
    }

    @Override
    public List<List<String>> anyItemsToString(List<Item> items) {
        List<List<String>> itemsAsStringList = new ArrayList<>();
        for (Item item: items) {
            ItemHandler itemHandler = ItemHandlerProvider.getHandlerByClass(item.getClass());
            itemsAsStringList.add(itemHandler.itemToString(item));
        }
        TableUtil tableUtil = new TableUtil(columnTitles,itemsAsStringList, out);
        tableUtil.validateRows();
        return tableUtil.getRows();
    }
}
