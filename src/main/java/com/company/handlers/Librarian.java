package com.company.handlers;

import com.company.ComparatorsForSorting;
import com.company.Validator;
import com.company.enums.SortingMenu;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.items.Item;
import com.company.table.TableUtil;
import com.company.work_with_files.FilesWorker;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

public abstract class Librarian {

    public FilesWorker workWithFiles;
    public PrintWriter out;

    Validator validator;



    public abstract boolean addItem(ItemHandler<? extends Item> itemHandler, List<String> itemOptions) throws IOException;

    public abstract boolean addItem(Item item) throws IOException ;


    public abstract boolean deleteItem(Integer itemID, boolean forBorrow) throws IOException ;

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

    public abstract void initSorting(ItemHandler<? extends Item> itemHandler) throws IOException ;

    public List<? extends Item> initSortingItemsByComparator(SortingMenu sortingParameter, ItemHandler<? extends Item> itemHandler) throws IOException {
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

