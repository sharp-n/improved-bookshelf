package com.company.handlers.item_handlers;

import com.company.*;
import com.company.enums.MainMenu;
import com.company.handlers.Librarian;
import com.company.items.Item;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.util.*;

import static com.company.Validator.BAD_NUMBER_VALIDATION_MESSAGE;
import static com.company.enums.ActionsWithItem.*;
import static com.company.enums.SortingMenu.*;
import static com.company.table.TableUtil.NEW_LINE;

@NoArgsConstructor
public abstract class ItemHandler<T extends Item> {

    //
    // journals deleting - 3.40 - with tests, 1.4- without
    // adding comics - 15.0
    // adding journals - 3.31
    //

    Scanner in; // TODO fix input/output
    PrintWriter out;
    public Validator validator;
    public UserInput userInput;

    public List<String> columnTitles = new ArrayList<>(Arrays.asList("item id","title","pages","borrowed","author", "publishing date"));

    public List<String> getColumnTitles() {
        return columnTitles;
    }

    protected ItemHandler(PrintWriter out, Scanner in) {
        this.out = out;
        this.in = in;
        this.validator = new Validator(out);
        this.userInput = new UserInput(out,in);
    }

    public abstract List<T> getSortedItemsByComparator(List<Item> items, Comparator<Item> comparator);

    public T createItem(List<String> options){
        return null;
    }

    public List<String> getItem() {
        Integer itemID = validator.validateID(userInput.idUserInput());

        if (itemID == null) {
            validator.printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
            return Collections.emptyList();
        }

        String title = validator.validateTitle(userInput.titleUserInput());
        if (!Librarian.checkItemForValidity(title)) return Collections.emptyList();

        Integer numOfPages = validator.validatePages(userInput.pagesUsersInput());
        if (numOfPages == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(itemID.toString(), title, numOfPages.toString());
    }

    public String initItemsMenuText(){
        return NEW_LINE + MainMenu.BOOK + NEW_LINE
                + MainMenu.NEWSPAPER + NEW_LINE
                + MainMenu.COMICS + NEW_LINE
                + MainMenu.JOURNAL + NEW_LINE;
    }

    public String initActionsWithItemsMenuText(){
        return NEW_LINE + ADD + NEW_LINE + DELETE +
                NEW_LINE + TAKE + NEW_LINE + RETURN +
                NEW_LINE + SHOW + NEW_LINE;
    }

    public String genSortingMenuText(){
        return NEW_LINE + EXIT_VALUE
                + NEW_LINE + ITEM_ID
                + NEW_LINE + TITLE
                + NEW_LINE + PAGES + NEW_LINE;
    }


    public List<List<String>> itemsToString(List<? extends Item> items, ItemHandler<? extends Item> itemHandler){
        List<List<String>> containersAsStringList = new ArrayList<>();
        items.forEach(i->containersAsStringList.add(itemHandler.itemToString(i)));
        return containersAsStringList;
    }

    public abstract List<List<String>> anyItemsToString(List<T> items);


    public String idToString(Item item){
        return Integer.toString(item.getItemID());
    }

    public String titleToString(Item item){
        return item.getTitle().trim();
    }

    public String pagesToString(Item item){
        return Integer.toString(item.getPages());
    }

    public String borrowedToString(Item item){
        return Boolean.toString(item.isBorrowed());
    }

    public List<String> itemToString(Item item){
        List<String> itemAsList = new ArrayList<>();
        itemAsList.add(idToString(item));
        itemAsList.add(titleToString(item));
        itemAsList.add(pagesToString(item));
        itemAsList.add(borrowedToString(item));
        return itemAsList;
    }


}