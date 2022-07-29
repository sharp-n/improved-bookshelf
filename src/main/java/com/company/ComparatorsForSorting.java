package com.company;

import com.company.items.Book;
import com.company.items.Item;

import java.util.Comparator;

public class ComparatorsForSorting {

    public static final Comparator<Item> COMPARATOR_ITEM_BY_ID = Comparator.comparing(Item::getItemID);
    public static final Comparator<Item> COMPARATOR_ITEM_BY_PAGES = Comparator.comparing(Item::getPages);
    public static final Comparator<Item> COMPARATOR_ITEM_BY_TITLE = Comparator.comparing(Item::getTitle);
    public static final Comparator<Item> COMPARATOR_ITEM_BY_DATE = Comparator.comparing(i->((Book)i).getPublishingDate());
    public static final Comparator<Item> COMPARATOR_ITEM_BY_AUTHOR = Comparator.comparing(i->((Book)i).getAuthor());

}
