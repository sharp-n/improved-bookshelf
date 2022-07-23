package com.company.handlers;

import com.company.items.Book;
import com.company.items.Newspaper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookHandler implements ItemHandler<Book>{

    @Override
    public List<Book> getSortedItemsByComparator(List<Book> items, Comparator<Book> comparator) {
        return items.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Book createItem(){

        return null;
    }
}
