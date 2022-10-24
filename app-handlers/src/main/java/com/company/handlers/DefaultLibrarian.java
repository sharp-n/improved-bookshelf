package com.company.handlers;

import com.company.Item;
import com.company.Validator;
import com.company.enums.SortingMenu;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.work_with_files.FilesWorker;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@NoArgsConstructor
public class DefaultLibrarian extends Librarian{

    private static final Logger log
            = Logger.getLogger(DefaultLibrarian.class);

    public boolean addItem(ItemHandler<? extends Item> itemHandler, List<String> itemOptions){
        try {
            Item item = itemHandler.createItem(itemOptions);
            if (checkIDForExistence(item.getItemID())) {
                out.println("Item with this ID exists. Please change ID and try again");
                return false;
            }
            workWithFiles.addItemToFile(item);
            out.println("Item was successful added");
            return true;
        } catch (IOException ioException){
            log.error(ioException.getMessage() + " : " + DefaultLibrarian.class.getSimpleName() + " : addItem()");
            return false;
        }
    }

    public DefaultLibrarian(FilesWorker workWithFiles, PrintWriter out) {
        this.workWithFiles = workWithFiles;
        this.out = out;
        this.validator = new Validator(out);
    }

    public boolean addItem(Item item) {
        try {
            if (checkIDForExistence(item.getItemID())) {
                out.println("Item with this ID exists. Please change ID and try again");
                return false;
            }
            workWithFiles.addItemToFile(item);
            out.println("Item was successful added");
            return true;
        } catch (IOException ioException) {
            log.error(ioException.getMessage() + " : " + DefaultLibrarian.class.getSimpleName() + " : addItem()");
            return false;
        }
    }

    public boolean deleteItem(Integer itemID, boolean forBorrow) {
        try {
            itemID = validator.validateIdToBorrow(itemID);
            if (itemID != null) {
                boolean deleted = false;
                if (checkIDForExistence(itemID)) {
                    deleted = workWithFiles.removeItemFromFile(itemID, forBorrow);
                }
                if (!deleted) {
                    out.println("This item maybe borrowed or does not exist");
                } else {
                    out.println("Item was successful deleted");
                }
                return deleted;
            }
            return false;
        } catch (IOException ioException) {
            log.error(ioException.getMessage() + " : " + DefaultLibrarian.class.getSimpleName() + " : deleteItem()");
            return false;
        }
    }

    public boolean borrowItem(Integer itemID, boolean borrow) {
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

    public void initSorting(ItemHandler<? extends Item> itemHandler) {
        try {
            Integer usersChoice = itemHandler.userInput.getSortingVar(itemHandler.genSortingMenuText());
            if (usersChoice != null) {
                SortingMenu sortingParameter = SortingMenu.getByIndex(usersChoice);
                List<? extends Item> sortedItemsByComparator = initSortingItemsByComparator(sortingParameter, itemHandler);
                if (!sortedItemsByComparator.isEmpty()) {
                    printItems(sortedItemsByComparator, itemHandler);
                }
            } else {
                itemHandler.userInput.printDefaultMessage();
            }
        }  catch (IOException ioException) {
            log.error(ioException.getMessage() + " : " + DefaultLibrarian.class.getSimpleName() + " : InitSorting()");
        }
    }
}
