package com.company.handlers;

import com.company.Librarian;
import com.company.convertors.BookConvertor;
import com.company.convertors.ItemConvertor;
import com.company.convertors.JournalConvertor;
import com.company.enums.SortingMenu;
import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.ConstantsForItemsTable.NEW_LINE;

@NoArgsConstructor // TODO
public class BookHandler extends ItemHandler<Book>{

    public BookHandler(PrintWriter out, Scanner in) {
        super(out,in);
    }

    @Override
    public List<Book> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator) {
        return cast(items).stream()
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

    @Override
    public List<List<String>> anyItemsToString(List<Book> books) {
        List<List<String>> booksAsStringList = new ArrayList<>();
        for (Book book: books) {
            ItemConvertor itemConvertor = new BookConvertor(book);
            booksAsStringList.add(itemConvertor.itemToString());
        }
        return booksAsStringList;
    }

    public List<Book> cast(List<Item> items) {
        List<Book> books = new ArrayList<>();
        items.forEach(i->books.add((Book)i));
        return books;
    }

    @Override
    public String genSortingMenuText() {
        return super.genSortingMenuText() + SortingMenu.AUTHOR
                + NEW_LINE + SortingMenu.PUBLISHING_DATE + NEW_LINE;
    }
}
