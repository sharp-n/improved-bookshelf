package com.company;

import com.company.items.Book;
import com.company.items.Item;
import com.company.server.ServerHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
public class Librarian {

    WorkWithFiles workWithFiles;
    ServerHandler serverHandler;

    public static final String TYPE_OF_ITEM_JOURNAL = "Journal";
    public static final String TYPE_OF_ITEM_BOOK = "Book";
    public static final String TYPE_OF_ITEM_NEWSPAPER = "Newspaper";

    public void addItem(Item item) throws IOException {
            workWithFiles.addItemToFile(item);
    }

    public boolean deleteItem(int itemID, boolean forBorrow, String typeOfItem) throws IOException {
        boolean deleted = false;
        if (checkIDForExistence(itemID, typeOfItem)) {
            deleted = workWithFiles.removeItemFromFile(itemID, forBorrow, typeOfItem);
        }
        if (!deleted) {
            serverHandler.writeLineMessage("This item maybe borrowed or does not exist");
        }
        return deleted;
    }

    public void borrowItem(int itemID, String typeOfItem, boolean borrow) throws IOException {
        List<Item> items = workWithFiles.readToItemsList();
        Item item = findItemByID(itemID, items);
        if (item != null) {
            if (item.isBorrowed() != borrow) {
                deleteItem(itemID, true, typeOfItem);
                item.setBorrowed(borrow);
                addItem(item);

                serverHandler.writeMessage("Success");
            } else if (borrow) {
                serverHandler.writeMessage("Item has already been taken by someone else");
            } else {
                serverHandler.writeMessage("Item has already been returned");
            }
        } else {
            serverHandler.writeLineMessage("There`s no such item");
        }
    }

    public List<? extends Item> sortingItemsByID(List<? extends Item> list) {
        return list.stream().sorted(Comparator.comparing(Item::getItemID)).collect(Collectors.toList());
    }

    public List<? extends Item> sortingItemsByTitle(List<? extends Item> list) {
        return list.stream().sorted(Comparator.comparing(Item::getTitle)).collect(Collectors.toList());
    }

    public List<? extends Item> sortingItemsByPages(List<? extends Item> list) {
        return list.stream().sorted(Comparator.comparing(Item::getPages)).collect(Collectors.toList());
    }

    public List<Book> sortingBooksByAuthor(List<Book> list) {
        return list.stream().sorted(Comparator.comparing(Book::getAuthor)).collect(Collectors.toList());
    }

    public List<Book> sortingBooksByPublishingDate(List<Book> list) {
        return list.stream().sorted(Comparator.comparing(Book::getPublishingDate)).collect(Collectors.toList());
    }

    public boolean checkIDForExistence(int itemID, String typeOfItem) throws IOException {
        List<? extends Item> items = new ArrayList<>();
        if (typeOfItem.equals(TYPE_OF_ITEM_BOOK)) {
            items = workWithFiles.readToBooksList();
        } else if (typeOfItem.equals(TYPE_OF_ITEM_JOURNAL)) {
            items = workWithFiles.readToJournalsList();
        } else if (typeOfItem.equals(TYPE_OF_ITEM_NEWSPAPER)) {
            items = workWithFiles.readToNewspapersList();
        }
        if (items != null) {
            for (Item item : items) {
                if (item.getItemID() == itemID) return true;
            }
            return false;
        }
        return false;
    }

    public static boolean checkItemForValidity(String item) {
        return item != null && !item.trim().equals("");
    }

    public static boolean checkItemForValidity(int item) {
        return item > 0;
    }

    private static Item findItemByID(int itemID, List<? extends Item> items) {
        for (Item item : items) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

}

