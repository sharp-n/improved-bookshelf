package com.company;

import com.company.items.Book;
import com.company.items.Item;

import java.util.Comparator;

public class ConstantsForSorting <T extends Item>{

    public final Comparator<T> COMPARATOR_ITEM_BY_ID = Comparator.comparing(Item::getItemID);
    public final Comparator<T> COMPARATOR_ITEM_BY_PAGES = Comparator.comparing(Item::getPages);
    public final Comparator<T> COMPARATOR_ITEM_BY_TITLE = Comparator.comparing(Item::getTitle);
    public final Comparator<Book> COMPARATOR_ITEM_BY_DATE = Comparator.comparing(Book::getPublishingDate);
    public final Comparator<Book> COMPARATOR_ITEM_BY_AUTHOR = Comparator.comparing(Book::getAuthor);

}
