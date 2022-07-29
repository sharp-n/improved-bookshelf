package com.company.handlers;

import com.company.Librarian;
import com.company.items.Book;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor // TODO
public class BookHandler extends ItemHandler<Book>{

    public BookHandler(PrintWriter out, Scanner in) {
        super(out,in);
    }

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
        int pages = Integer.parseInt(options.get(2));
        String author = options.get(3);
        String [] date = options.get(4).split("\\.");

        GregorianCalendar publishingDate = validateDate(Integer.parseInt(date[0].trim()),Integer.parseInt(date[1].trim()),Integer.parseInt(date[2].trim()));
        if (publishingDate == null) {return null;}

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

    public List<String> getItem() {
        String author = "";
        List<String> itemOptions = new ArrayList<>(super.getItem());

        author = validator.validateAuthorName(userInput.authorUserInput());
        if (author == null) {
            return null;
        }

        Integer publishingYear = userInput.yearUserInput();
        Integer publishingMonth = userInput.monthUserInput();
        Integer publishingDay = userInput.dayUserInput();

        itemOptions.add(author);
        itemOptions.add(publishingYear + " . " + publishingMonth + " . " + publishingDay);

        return itemOptions;

    }

}
