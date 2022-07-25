package com.company.handlers;

import com.company.Librarian;
import com.company.items.Book;
import com.company.items.Newspaper;

import java.util.Comparator;
import java.util.GregorianCalendar;
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
    public Book createItem(List<String> options){
        int itemID = Integer.parseInt(options.get(0));
        String title = options.get(1);
        String author = options.get(2);
        String [] date = options.get(3).split("\\.");
        GregorianCalendar publishingDate = new GregorianCalendar(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
        int pages = Integer.parseInt(options.get(4));
        return new Book(itemID,title, author, publishingDate,pages);
    }


    public GregorianCalendar validateDate(Integer year, Integer month, Integer day) {
        if (day == null || month == null || year == null) {
            return null;
        }
        if (day > 31) {
            return null;
        }
        if (month > 12) {
            return null;
        }
        if (Librarian.checkItemForValidity(year) && Librarian.checkItemForValidity(month)
                && Librarian.checkItemForValidity(day)) {
            return new GregorianCalendar(year, month - 1, day);
        }
        return null;
    }
}
