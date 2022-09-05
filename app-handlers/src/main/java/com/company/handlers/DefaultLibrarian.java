package com.company.handlers;

import com.company.Item;
import com.company.Validator;
import com.company.enums.SortingMenu;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.work_with_files.FilesWorker;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@NoArgsConstructor
public class DefaultLibrarian extends Librarian{

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

    public DefaultLibrarian(FilesWorker workWithFiles, PrintWriter out) {
        this.workWithFiles = workWithFiles;
        this.out = out;
        this.validator = new Validator(out);
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

    public void initSorting(ItemHandler<? extends Item> itemHandler) throws IOException {
        Integer usersChoice = itemHandler.userInput.getSortingVar(itemHandler.genSortingMenuText());
        if (usersChoice != null) {
            SortingMenu sortingParameter = SortingMenu.getByIndex(usersChoice);
            List<? extends Item> sortedItemsByComparator = initSortingItemsByComparator(sortingParameter,itemHandler);
            if(!sortedItemsByComparator.isEmpty()){
                printItems(sortedItemsByComparator, itemHandler);
            }
        } else{
            itemHandler.userInput.printDefaultMessage();
        }
    }
}
