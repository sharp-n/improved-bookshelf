package com.company.handlers;

import com.company.*;
import com.company.convertors.BookConvertor;
import com.company.convertors.ItemConvertor;
import com.company.convertors.JournalConvertor;
import com.company.convertors.NewspaperConvertor;
import com.company.enums.MainMenu;
import com.company.items.Book;
import com.company.items.Item;
import com.company.items.Journal;
import com.company.items.Newspaper;
import lombok.NoArgsConstructor;

import javax.security.sasl.AuthorizeCallback;
import java.io.PrintWriter;
import java.util.*;

import static com.company.ConstantsForItemsTable.NEW_LINE;
import static com.company.Validator.BAD_NUMBER_VALIDATION_MESSAGE;
import static com.company.enums.ActionsWithItem.*;
import static com.company.enums.SortingMenu.*;

@NoArgsConstructor
public abstract class ItemHandler<T extends Item> {

    Scanner in; // TODO fix input/output
    PrintWriter out;
    public Validator validator;
    public UserInput userInput;

    public ItemHandler(PrintWriter out, Scanner in) {
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
        return NEW_LINE + MainMenu.BOOK + NEW_LINE + MainMenu.JOURNAL + NEW_LINE  + MainMenu.NEWSPAPER + NEW_LINE;
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


    public List<List<String>> itemsToString(List<? extends Item> items){ // TODO remove instanceof
        List<List<String>> containersAsStringList = new ArrayList<>();
        ItemConvertor itemConvertor;
        for (Item item: items){
            if (item instanceof Book){
                itemConvertor = new BookConvertor(((Book)item));
                containersAsStringList.add(itemConvertor.itemToString());
            } else if (item instanceof Journal){
                itemConvertor = new JournalConvertor(((Journal)item));
                containersAsStringList.add(itemConvertor.itemToString());
            }
            else if (item instanceof Newspaper){
                itemConvertor = new NewspaperConvertor((Newspaper)item);
                containersAsStringList.add(itemConvertor.itemToString());
            }
        }
        return containersAsStringList;
    }

    public abstract List<List<String>> anyItemsToString(List<T> items);



}
