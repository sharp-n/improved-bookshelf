package com.company.handlers;

import com.company.Librarian;
import com.company.enums.SortingMenu;
import com.company.items.Book;
import com.company.items.Item;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
            booksAsStringList.add(itemToString(book));
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

    public String authorToString(Book book){
        if(book.getAuthor()==null) return "NULL";
        return book.getAuthor();
    }

    public String publishingDateToString(Book book){
        SimpleDateFormat df = new SimpleDateFormat("dd.M.y");
        if(book.getPublishingDate()==null) return "NULL";
        return df.format(book.getPublishingDate().getTime());
    }

    @Override
    public List<String> itemToString(Item item){
        List<String> bookAsList = new ArrayList<>();
        bookAsList.add(idToString(item));
        bookAsList.add(titleToString(item));
        bookAsList.add(authorToString((Book)item));
        bookAsList.add(publishingDateToString((Book)item));
        bookAsList.add(pagesToString(item));
        bookAsList.add(borrowedToString(item));
        return bookAsList;
    }
}
