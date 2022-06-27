package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Librarian {

    public static void addItem(Item item) throws IOException {
        if(item instanceof Book){
            List<Book> books = WorkWithFiles.readToBooksList();
            if (books==null) {
                books = new ArrayList<>();
            }
            books.add((Book)item);
            WorkWithFiles.writeList(books);
        } else {
            List<Journal> journals = WorkWithFiles.readToJournalsList();
            if (journals==null) {
                journals = new ArrayList<>();
            }
            journals.add((Journal)item);
            WorkWithFiles.writeList(journals);
        }
    }

    public static boolean deleteBook(int itemID, boolean forBorrow) throws IOException {
        if (checkIDForExistence(itemID, new Book())) {
            List<Book> books = WorkWithFiles.readToBooksList();
            if (books != null) {
                for (Book book : books) {
                    if (book.getItemID() == itemID) {
                        return defineIfCanDelete(books, book,forBorrow);
                    }
                }
            } else {
                Dialogues.printItemNotExistsMessage();
            }
        }
        return false;
    }

    public static boolean deleteJournal(int itemID, boolean forBorrow) throws IOException {
        if (checkIDForExistence(itemID,new Journal())) {
            List<Journal> journals = WorkWithFiles.readToJournalsList();
            if (journals != null) {
                for (Journal journal : journals) {
                    if (journal.getItemID() == itemID) {
                        return defineIfCanDelete(journals, journal,forBorrow);
                    }
                }
            } else {
                Dialogues.printItemNotExistsMessage();
            }
        }
        return false;
    }

    public static boolean defineIfCanDelete(List<? extends Item> items, Item item, boolean forBorrow) {
        if (!forBorrow) {
            if (item.isBorrowed()) {
                System.out.println("This item is borrowed, please return it first");
                return false;
            }
        }
        items.remove(item);
        WorkWithFiles.writeList(items);
        return true;
    }

    public static void borrowItem(int itemID, Item typeOfItem, boolean borrow) throws IOException {
        Item item;
        if(typeOfItem instanceof Book){
            List<Book> items = WorkWithFiles.readToBooksList();
            item = findItemByID(itemID,items);
        } else {
            List<Journal> items = WorkWithFiles.readToJournalsList();
            item = findItemByID(itemID,items);
        }
        if (item != null) {
            if (item.isBorrowed() != borrow) {
                if (typeOfItem instanceof Book){
                    deleteBook(itemID, true);
                } else deleteJournal(itemID,true);
                item.setBorrowed(borrow);
                addItem(item);
                System.out.println("Success");
            } else if (borrow) System.out.println("Item has already been taken by someone else");
            else System.out.println("Item has already been returned");
        } else Dialogues.printItemNotExistsMessage();
    }

    public static List<? extends Item> sortingItemsByID(List<? extends Item> list) {
        return list.stream().sorted(Comparator.comparing(Item::getItemID)).collect(Collectors.toList());
    }

    public static List<? extends Item> sortingItemsByTitle(List<? extends Item> list) {
        return list.stream().sorted(Comparator.comparing(Item::getTitle)).collect(Collectors.toList());
    }

    public static List<? extends Item> sortingItemsByPages(List<? extends Item> list) {
        return list.stream().sorted(Comparator.comparing(Item::getPages)).collect(Collectors.toList());
    }

    public static List<Book> sortingBooksByAuthor(List<Book> list) {
        return list.stream().sorted(Comparator.comparing(Book::getAuthor)).collect(Collectors.toList());
    }

    public static List<Book> sortingBooksByPublishingDate(List<Book> list) {
        return list.stream().sorted(Comparator.comparing(Book::getPublishingDate)).collect(Collectors.toList());
    }

    public static boolean checkIDForExistence(int itemID, Item itemType) throws IOException {
        List<? extends Item> items ;
        if (itemType instanceof Book){
            items = WorkWithFiles.readToBooksList();
        } else items = WorkWithFiles.readToJournalsList();
        if (items != null) {
            for (Item item : items) {
                if (item.getItemID() == itemID) return true;
            }
            return false;
        }
        return false;
    }

    public static boolean checkItemForValidity(String item){
        return item!=null&&!item.trim().equals("");
    }

    public static boolean checkItemForValidity(int item){
        return item>0;
    }

    private static Item findItemByID(int itemID, List<?extends Item> items){
        for (Item item : items) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

}

