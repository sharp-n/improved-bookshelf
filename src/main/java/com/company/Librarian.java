package com.company;

import com.company.convertors.ItemsConvertor;
import com.company.items.Item;
import com.company.table.TableUtil;
import com.company.work_with_files.WorkWithFiles;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Librarian {

    public WorkWithFiles workWithFiles;
    public PrintWriter out;

    public void addItem(Item item) throws IOException {
        workWithFiles.addItemToFile(item);
    }

    public boolean deleteItem(int itemID, boolean forBorrow) throws IOException {
        boolean deleted = false;
        if (checkIDForExistence(itemID)) {
            deleted = workWithFiles.removeItemFromFile(itemID, forBorrow);
        }
        if (!deleted) {
            out.println("This item maybe borrowed or does not exist");
        }
        return deleted;
    }

    public void borrowItem(int itemID, boolean borrow) throws IOException {
        List<Item> items = workWithFiles.readToItemsList();
        Item item = findItemByID(itemID, items);
        if (item != null) {
            if (item.isBorrowed() != borrow) {
                deleteItem(itemID, true);
                item.setBorrowed(borrow);
                addItem(item);

                out.println("Success");
            } else if (borrow) {
                out.println("Item has already been taken by someone else");
            } else {
                out.println("Item has already been returned");
            }
        } else {
            out.println("There`s no such item");
        }
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

    public <T extends Item> void printItems(List<T> items) {
        if (items.isEmpty()) out.println("There`s no items here");
        else {
            ItemsConvertor itemsConvertor = new ItemsConvertor();
            List<List<String>> strItems = itemsConvertor.itemsToString(items);
            List<String> options = generateOptionsForTable(defineNumberOfColumns(strItems));
            TableUtil tableUtil = new TableUtil(options, strItems, out);
            tableUtil.printTable();
        }
    }

    public List<String> generateOptionsForTable(int numberOfColumns) {
        return new ConstantsForItemsTable().allTheColumnsForItems.get(numberOfColumns);
    }

    public int defineNumberOfColumns(List<List<String>> items) {
        int maxNumOfColumns = 0;
        for (List<String> item : items) {
            int numOfColumns = 0;
            for (String option : item) {
                numOfColumns++;
            }
            if (numOfColumns > maxNumOfColumns) {
                maxNumOfColumns = numOfColumns;
            }
        }
        return maxNumOfColumns;
    }

}

