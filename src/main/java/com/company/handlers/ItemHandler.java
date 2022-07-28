package com.company.handlers;

import com.company.*;
import com.company.enums.MainMenu;
import com.company.items.Item;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static com.company.ConstantsForItemsTable.NEW_LINE;
import static com.company.Validator.BAD_NUMBER_VALIDATION_MESSAGE;
import static com.company.enums.ActionsWithBook.*;
import static com.company.enums.ActionsWithItem.*;

@NoArgsConstructor
public class ItemHandler<T extends Item> {

    Scanner in; // TODO fix input/output
    PrintWriter out;

    //public Librarian librarian;
    public Validator validator;
    public UserInput userInput;

    public ItemHandler(PrintWriter out, Scanner in) {
        this.out = out;
        this.in = in;
        this.validator = new Validator(out);
        this.userInput = new UserInput(out,in);
    }

    public List<T> getSortedItemsByComparator(List<T> items, Comparator<T> comparator){
        return null;
    }

    public void addItem(Librarian librarian) throws IOException {
        Item item = createItem(getItem(librarian));
        if (item == null) {
            out.println("Try again");
        } else {
            librarian.addItem(item);
            userInput.printSuccessMessage("added");
        }
    }

    public T createItem(List<String> options){
        return null;
    }

    public List<String> getItem(Librarian librarian) throws IOException {
        Integer itemID = validator.validateID(userInput.idUserInput());

        if (itemID == null) {
            validator.printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
            return Collections.emptyList();
        }

        if (librarian.checkIDForExistence(itemID)) {
            out.println("Item with this ID exists. Please change ID and try again");
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

    public void deleteItem(Librarian librarian) throws IOException {
        Integer itemID = validator.validateIdToBorrow(userInput.idUserInput());
        if (itemID != null) {
            boolean deleted = librarian.deleteItem(itemID, false);
            if (deleted) {
                userInput.printSuccessMessage("deleted");
            }
        }
    }

    public void initItemBorrowing(boolean borrow, Librarian librarian) throws IOException {
        Integer itemID = validator.validateIdToBorrow(userInput.idUserInput());
        if (itemID != null) {
            librarian.borrowItem(itemID, borrow);
        }
    }

    public String initItemsMenuText(){
        return NEW_LINE + MainMenu.BOOK + NEW_LINE + MainMenu.JOURNAL + NEW_LINE  + MainMenu.NEWSPAPER + NEW_LINE;
    }

    public String initActionsWithItemsMenuText(){
        return NEW_LINE + ADD + NEW_LINE + DELETE +
                NEW_LINE + TAKE + NEW_LINE + RETURN +
                NEW_LINE + SHOW + NEW_LINE;
    }
}
