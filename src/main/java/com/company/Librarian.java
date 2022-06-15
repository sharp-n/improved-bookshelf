package com.company;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Librarian {

    public static void addItem(Item item, String filePath) {
        Gson gson = new Gson();
        String itemStr = gson.toJson(item);
        WorkWithFiles.write(itemStr, filePath, true);
    }

    public static boolean deleteItem(int itemID, String filePath, boolean forBorrow) {
        if (checkIDForExistence(itemID, filePath)) {
            List<String> lines = WorkWithFiles.readToList(filePath);
            if (lines != null) {
                Gson gson = new Gson();
                for (String line : lines) {
                    Item fromGsItem = gson.fromJson(line, Item.class);
                    if (fromGsItem.getItemID() == itemID) {
                        if (!forBorrow){
                            if (fromGsItem.isBorrowed()) {
                                System.out.println("This item is borrowed, please return it first");
                                return false;
                            } else {
                                lines.remove(line);
                                break;
                            }
                        } else {
                            lines.remove(line);
                            break;
                        }
                    }
                }
                WorkWithFiles.clearFile(filePath);
                for (String line : lines)
                    WorkWithFiles.write(line, filePath, true);
                return true;
            }
        } else {
            Dialogues.printItemNotExistsMessage();
        }
        return false;
    }

    public static void borrowItem(int itemID, String filePath, Item typeOfItem, boolean borrow) {
        Item item = takeItemFromFile(itemID, filePath, typeOfItem);
        if (item != null) {
            if (item.isBorrowed() != borrow) {
                deleteItem(itemID, filePath, true);
                item.setBorrowed(borrow);
                addItem(item, filePath);
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

    public static boolean checkIDForExistence(int itemID, String filePath) {
        List<String> lines = WorkWithFiles.readToList(filePath);
        if (lines != null) {
            Gson gson = new Gson();
            for (String line : lines) {
                Item fromGsItem = gson.fromJson(line, Item.class);
                if (fromGsItem.getItemID() == itemID) return true;
            }
            return false;
        }
        return true;
    }

    private static Item takeItemFromFile(int itemID, String filePath, Item typeOfItem) {
        List<String> lines = WorkWithFiles.readToList(filePath);
        Item fromGsItem = new Item();
        Gson gson = new Gson();
        for (String line : lines) {
            if (typeOfItem.getClass() == Book.class) {
                fromGsItem = gson.fromJson(line, Book.class);
            } else fromGsItem = gson.fromJson(line, Journal.class);
            if (fromGsItem.getItemID() == itemID) return fromGsItem;
        }
        return null;
    }

    public static List<Book> convertBookListFromGson(List<String> lines){
        if(lines==null) return new ArrayList<>();
        Gson gson = new Gson();
        List<Book> items = new ArrayList<>();
        for (String line : lines) {
            items.add(gson.fromJson(line, Book.class));
        }
        return items;
    }
    public static List<Journal> convertJournalListFromGson(List<String> lines){
        if(lines==null) return new ArrayList<>();
        Gson gson = new Gson();
        List<Journal> items = new ArrayList<>();
        for (String line : lines) {
            items.add(gson.fromJson(line, Journal.class));
        }
        return items;
    }

    public static boolean checkItemForValidity(String item){
        return item!=null&&!item.trim().equals("");
    }

    public static boolean checkItemForValidity(int item){
        return item>0;
    }

}
