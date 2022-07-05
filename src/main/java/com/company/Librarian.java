package com.company;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    public String filePath;

    public void addItem(Item item) throws IOException {
        if(item instanceof Book){WorkWithFiles.addItemToFile(new Container<>((Book) item),filePath);}
        if(item instanceof Journal){WorkWithFiles.addItemToFile(new Container<>((Journal) item),filePath);}
    }

    public boolean deleteItem(int itemID, boolean forBorrow, String typeOfItem) throws IOException {
        if (checkIDForExistence(itemID,typeOfItem)) {
            boolean deleted = false;
            JsonArray jsonArray = WorkWithFiles.makeJsonArrayFromFile(filePath);
            List<Container<? extends Item>> containers = new ArrayList<>();
            for (JsonElement element: jsonArray) {
                containers.add(WorkWithFiles.gson.fromJson(element, Container.class));
                JsonObject itemObject = element.getAsJsonObject().getAsJsonObject("item");
                String typeOfClass = element.getAsJsonObject().get("typeOfClass").getAsString();
                if (typeOfClass.equals(typeOfItem)) {
                    if (WorkWithFiles.gson.fromJson(itemObject, Journal.class).itemID == itemID){
                        if(!defineIfCanDelete(element,forBorrow)){return false;}
                        containers.remove(containers.size()-1);
                        deleted = true;
                    }
                }
            }
            if (deleted){
                WorkWithFiles.rewriteFile(containers,filePath);
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean defineIfCanDelete(JsonElement itemObject, boolean forBorrow) {
        if (!forBorrow) {
            if (WorkWithFiles.gson.fromJson(itemObject, Journal.class).isBorrowed()) {
                System.out.println("This item is borrowed, please return it first");
                return false;
            }
        }
        return true;
    }

    public void borrowItem(int itemID, String typeOfItem, boolean borrow) throws IOException {
        Item item;
        if(typeOfItem.equals("Book")){
            List<Book> items = WorkWithFiles.readToBooksList(filePath);
            item = findItemByID(itemID,items);
        } else {
            List<Journal> items = WorkWithFiles.readToJournalsList(filePath);
            item = findItemByID(itemID,items);
        }
        if (item != null) {
            if (item.isBorrowed() != borrow) {
                deleteItem(itemID,true, typeOfItem);
                item.setBorrowed(borrow);
                addItem(item);
                System.out.println("Success");
            } else if (borrow) System.out.println("Item has already been taken by someone else");
            else System.out.println("Item has already been returned");
        } else Dialogues.printItemNotExistsMessage();
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
        List<? extends Item> items ;
        if (typeOfItem.equals("Book")){
            items = WorkWithFiles.readToBooksList(filePath);
        } else items = WorkWithFiles.readToJournalsList(filePath);
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

