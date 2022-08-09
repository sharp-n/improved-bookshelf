package com.company.handlers;

import com.company.ComparatorsForSorting;
import com.company.Validator;
import com.company.enums.SortingMenu;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.items.Item;
import com.company.table.TableUtil;
import com.company.work_with_files.FilesWorker;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class Librarian {

    public FilesWorker workWithFiles;
    public PrintWriter out;

    Validator validator;

    public Librarian(FilesWorker workWithFiles, PrintWriter out) {
        this.workWithFiles = workWithFiles;
        this.out = out;
        this.validator = new Validator(out);
    }

    public boolean addItem(ItemHandler<? extends Item> itemHandler, List<String> itemOptions) throws IOException {
        Item item = itemHandler.createItem(itemOptions);
        if (checkIDForExistence(item.getItemID())) {
            out.println("Item with this ID exists. Please change ID and try again");
            return false;
        }
        workWithFiles.addItemToFile(item);
        out.println("Item was successful added");
        return true;
    }

    public boolean addItem(Item item) throws IOException {
        if (checkIDForExistence(item.getItemID())) {
            out.println("Item with this ID exists. Please change ID and try again");
            return false;
        }
        workWithFiles.addItemToFile(item);
        out.println("Item was successful added");
        return true;
    }

    public boolean deleteItem(Integer itemID, boolean forBorrow) throws IOException {
        itemID = validator.validateIdToBorrow(itemID);
        if (itemID != null) {
            boolean deleted = false;
            if (checkIDForExistence(itemID)) {
                deleted = workWithFiles.removeItemFromFile(itemID, forBorrow);
            }
            if (!deleted) {
                out.println("This item maybe borrowed or does not exist");
            }else {
                out.println("Item was successful deleted");
            }
            return deleted;
        }
        return false;
    }

    public boolean borrowItem(Integer itemID, boolean borrow) throws IOException {
        itemID = validator.validateIdToBorrow(itemID);
        if (itemID != null) {
            List<Item> items = workWithFiles.readToItemsList();
            Item item = findItemByID(itemID, items);
            if (item != null&&item.isBorrowed() != borrow) {
                deleteItem(itemID, true);
                item.setBorrowed(borrow);
                addItem(item);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean checkIDForExistence(int itemID) throws IOException {
        List<? extends Item> items = workWithFiles.readToItemsList();
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

    public static Item findItemByID(int itemID, List<? extends Item> items) {
        for (Item item : items) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public <T extends Item> void printItems(List<T> items, ItemHandler<? extends Item> itemHandler) {
        if (items.isEmpty()) out.println("There`s no items here");
        else {
            List<List<String>> strItems = itemHandler.itemsToString(items,itemHandler);
            List<String> options = itemHandler.getColumnTitles();
            TableUtil tableUtil = new TableUtil(options, strItems, out);
            tableUtil.printTable();
        }
    }

    public void initSorting(ItemHandler<? extends Item> itemHandler) throws IOException {
        Integer usersChoice = itemHandler.userInput.getSortingVar(itemHandler.genSortingMenuText());
        if (usersChoice != null) {
            SortingMenu sortingParameter = SortingMenu.getByIndex(usersChoice);
            List<? extends Item> sortedItemsByComparator = initSortingItemsByComparator(workWithFiles, sortingParameter,itemHandler);
            if(!sortedItemsByComparator.isEmpty()){
                printItems(sortedItemsByComparator, itemHandler);
            }
        } else{
            itemHandler.userInput.printDefaultMessage();
        }
    }

    public List<? extends Item> initSortingItemsByComparator(FilesWorker workWithFiles, SortingMenu sortingParameter, ItemHandler<? extends Item> itemHandler) throws IOException {
        String typeOfItem = ItemHandlerProvider.getClassByHandler(itemHandler).getSimpleName();
        List<Item> items =   workWithFiles.readToAnyItemList(typeOfItem);
        switch (sortingParameter) {
            case RETURN_VALUE:
                break;
            case ITEM_ID:
                return itemHandler.getSortedItemsByComparator(items, ComparatorsForSorting.COMPARATOR_ITEM_BY_ID);
            case TITLE:
                return itemHandler.getSortedItemsByComparator(items, ComparatorsForSorting.COMPARATOR_ITEM_BY_TITLE);
            case PAGES:
                return itemHandler.getSortedItemsByComparator(items, ComparatorsForSorting.COMPARATOR_ITEM_BY_PAGES);
            case AUTHOR:
                return itemHandler.getSortedItemsByComparator(items, ComparatorsForSorting.COMPARATOR_ITEM_BY_AUTHOR);
            case PUBLISHING_DATE:
                return itemHandler.getSortedItemsByComparator(items, ComparatorsForSorting.COMPARATOR_ITEM_BY_DATE);
            default:
                itemHandler.userInput.printDefaultMessage();
                break;
        }
        return Collections.emptyList();
    }

}

