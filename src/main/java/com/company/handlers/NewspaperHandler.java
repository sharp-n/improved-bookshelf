package com.company.handlers;

import com.company.items.Newspaper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NewspaperHandler implements ItemHandler<Newspaper>{

    @Override
    public List<Newspaper> getSortedItemsByComparator(List<Newspaper> items, Comparator<Newspaper> comparator) {
        return items.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Newspaper createItem(List<String> options){
        int itemID = Integer.parseInt(options.get(0));
        String title = options.get(1);
        int pages = Integer.parseInt(options.get(4));
        return new Newspaper(itemID,title,pages);
    }

}
